mkdir /home/andre/data/
mkdir /home/andre/data/r1s1
echo Executando MongoD...
mongod --port 27001 --dbpath /home/andre/data/r1s1 --logpath /home/andre/data/r1s1.log --fork
