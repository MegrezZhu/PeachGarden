const cio = require('cheerio');
const fs = require('fs-extra');
const ax = require('axios').create({
  baseURL: 'http://cd.e3ol.com',
  timeout: 2000,
  headers: {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36'
  }
});
const Queue = require('./queue');

const GeneralNum = 6163;
const result = [];

(async () => {
  const queue = new Queue(10);
  queue.push(...Array(GeneralNum)
    .fill(null)
    .map((_, idx) => idx + 1)
    .map(id => {
      const fn = fetch.bind(null, id);
      fn.id = id;
      return fn;
    })
  );
  let count = 0;
  queue.on('error', (err, job) => {
    if (!err.message.match(/timeout/)) {
      console.error(err);
    }
    queue.push(job); // retry
  });
  queue.on('complete', job => {
    console.log(`${job.id} checked. [${++count}/${GeneralNum}]`);
    if (count % 1000 === 0) {
      fs.writeJson('./result/all.json', result.sort((o1, o2) => o1.id > o2.id ? 1 : -1), { spaces: 2 }); // save per stage
    }
  });

  await queue.wait();
  console.log(`all done, ${result.length} in total`);
  await fs.writeJson('./result/all.json', result.sort((o1, o2) => o1.id > o2.id ? 1 : -1), { spaces: 2 });
})()
  .catch(console.error);

async function fetch (id) {
  const { data } = await ax.get(`/view_2.asp?id=${id}`);
  const $ = cio.load(data);
  if (isEmpty($)) return;
  const info = {
    id, ...extract($)
  };
  await getRate(info);
  result.push(afterprocess(info));
}

function isEmpty ($) {
  return $('.view_title .a2').text() === '资料空缺';
}

// exrtact basic infomations from e3ol website
function extract ($) {
  let avatar = $('#view_div .pk_pic_off_l').attr('style');
  let matchRes = avatar.match(/url\((.+)\)/);
  avatar = matchRes ? matchRes[1] : null;

  let [, name, pinyin] = $('#view_div .a2').html().match(/(.+)<br>(.+)/);
  name = $(`<span>${name}</span>`).text();
  pinyin = $(`<span>${pinyin}</span>`).text();

  const abstract = $('.a3').text();
  $('.view_content div:first-child').remove();
  const description = $('.view_content').text().trim();

  return {
    avatar, name, pinyin, abstract, description
  };
}

// extract infomation from absctracts
function afterprocess (item) {
  const gender = item.abstract.match(/[\u4e00-\u9fa5]+\W+男/) ? 1 : 0;
  let matched = item.abstract.match(/生卒（(.+) - (.+?)）/);
  const from = matched && matched[1] !== '？' ? Number(matched[1]) : null;
  const to = matched && matched[2] !== '？' ? Number(matched[2]) : null;

  matched = item.abstract.match(/籍贯：([\u4e00-\u9fa5]+（[\u4e00-\u9fa5]+）?)/);
  const origin = matched ? matched[1] : null;
  return {
    ...item,
    gender,
    from,
    to,
    origin
  };
}

// get relative count from baidu
async function getRate (item) {
  const { name } = item;

  const { data } = await ax.get(`http://www.baidu.com/s?ie=UTF-8&wd=${encodeURIComponent(name)}`);
  const $ = cio.load(data);
  const matched = $('.head_nums_cont_outer .nums').text().match(/百度为您找到相关结果约([\d,]+)个/);
  if (!matched) throw new Error('timeout'); // fake timeout
  item.baiduCount = Number(matched[1].replace(/,/g, ''));

  return item;
}
