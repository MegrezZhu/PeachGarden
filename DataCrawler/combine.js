const fs = require('fs-extra');

(async () => {
  const kingdoms = await fs.readJson('./result/kingdoms.json');
  const peoples = await fs.readJson('./result/all.json');
  const nameMap = new Map();
  peoples.forEach(p => {
    p.belong = p.belongId = null;
    nameMap.set(p.name, p);
  });

  for (const {id, name, members} of kingdoms) {
    for (const p of members) {
      const people = nameMap.get(p);
      if (!people) continue;
      people.belong = name;
      people.belongId = id;
    }
  }

  await fs.writeJson('./result/final.json', peoples, { spaces: 2 });
  console.log(`combine done`);
})()
  .catch(console.error);
