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

const kingdoms = {
  '1': {
    id: 1,
    name: '东汉',
    pageNum: 51,
    members: []
  },
  '2': {
    id: 2,
    name: '魏',
    pageNum: 60,
    members: []
  },
  '3': {
    id: 3,
    name: '蜀',
    pageNum: 19,
    members: []
  },
  '4': {
    id: 4,
    name: '吴',
    pageNum: 37,
    members: []
  },
  '5': {
    id: 5,
    name: '袁绍',
    pageNum: 4,
    members: []
  },
  '6': {
    id: 6,
    name: '袁术',
    pageNum: 2,
    members: []
  },
  '7': {
    id: 7,
    name: '刘表',
    pageNum: 2,
    members: []
  },
  '10': {
    id: 10,
    name: '起义军',
    pageNum: 6,
    members: []
  },
  '16': {
    id: 16,
    name: '董卓',
    pageNum: 2,
    members: []
  },
  '19': {
    id: 19,
    name: '刘璋',
    pageNum: 2,
    members: []
  },
  '25': {
    id: 25,
    name: '西晋',
    pageNum: 22,
    members: []
  },
  '26': {
    id: 26,
    name: '少数民族',
    pageNum: 7,
    members: []
  },
  '22': {
    id: 22,
    name: '在野',
    pageNum: 8,
    members: []
  },
  '34': {
    id: 34,
    name: '其他',
    pageNum: 10,
    members: []
  }
};

(async () => {
  const queue = new Queue(20);
  for (const [id, info] of Object.entries(kingdoms)) {
    queue.push(...createTasks([id, info]));
  }
  queue.on('error', (err, job) => {
    if (err.message.match(/timeout/)) {
      queue.push(job); // retry
    } else throw err;
  });
  queue.on('complete', job => {
    console.log(`${job.id} checked.`);
  });

  await queue.wait();
  const res = Object.values(kingdoms);
  await fs.writeJson('./result/kingdoms.json', res, { spaces: 2 });
  console.log(`done, total: ${res.reduce((sum, kingdom) => sum + kingdom.members.length, 0)}`);
})()
  .catch(console.error);

function genURL (id, page) {
  return `/web_2.asp?a1=&a2=${id}&a3=&a4=&a5=&a6=&a7=&a8=&a9=&a10=&a12=&pageno=${page}`;
}

function createTasks ([id, info]) {
  return Array(info.pageNum).fill(null).map((_, page) => {
    const fn = getKingdom.bind(null, id, page + 1);
    fn.id = `${info.name} pn: ${page + 1}`;
    return fn;
  });
}

async function getKingdom (id, page) {
  let data;
  try {
    data = (await ax.get(genURL(id, page))).data;
  } catch (err) {
    if (err.response && err.response.status === 500 && id === '34' && page === 3) {
      data = err.response.data;
    } else throw err;
  }
  const $ = cio.load(data);
  $('.book_2_list .list_right span').each((_, ele) => {
    kingdoms[id].members.push($(ele).text());
  });
}
