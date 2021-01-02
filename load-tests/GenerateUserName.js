const objectstocsv = require('objects-to-csv')
const faker = require('faker')

let data = []
let number = 60

for(let i=0;i<number;i++){
    data.push(
        {
            USERNAME: faker.name.firstName() + faker.random.word(),
        }
    )
}

// convert to csv file
const csv = new objectstocsv(data);

// Save to file:
csv.toDisk('./UserName.csv');
