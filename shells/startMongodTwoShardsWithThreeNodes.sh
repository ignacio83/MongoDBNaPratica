mkdir /home/andre/data/
mkdir /home/andre/data/r1s1
mkdir /home/andre/data/r2s1
mkdir /home/andre/data/r3s1

mkdir /home/andre/data/r1s2
mkdir /home/andre/data/r2s2
mkdir /home/andre/data/r3s2
mkdir /home/andre/data/cfg

echo Executando Mongod - Nó 1 - S1
mongod --port 27001 --shardsvr --replSet s1 --dbpath /home/andre/data/r1s1 --logpath /home/andre/data/r1s1.log --smallfiles --oplogSize 100 --fork
echo Executando Mongod - Nó 2 - S1
mongod --port 27002 --shardsvr --replSet s1 --dbpath /home/andre/data/r2s1 --logpath /home/andre/data/r2s1.log --smallfiles --oplogSize 100 --fork
echo Executando Mongod - Nó 3 - S1
mongod --port 27003 --shardsvr --replSet s1 --dbpath /home/andre/data/r3s1 --logpath /home/andre/data/r3s1.log --smallfiles --oplogSize 100 --fork

echo Executando Mongod - Nó 1 - S2
mongod --port 27004 --shardsvr --replSet s2 --dbpath /home/andre/data/r1s2 --logpath /home/andre/data/r1s2.log --smallfiles --oplogSize 100 --fork
echo Executando Mongod - Nó 2 - S2
mongod --port 27005 --shardsvr --replSet s2 --dbpath /home/andre/data/r2s2 --logpath /home/andre/data/r2s2.log --smallfiles --oplogSize 100 --fork
echo Executando Mongod - Nó 3 - S2
mongod --port 27006 --shardsvr --replSet s2 --dbpath /home/andre/data/r3s2 --logpath /home/andre/data/r3s2.log --smallfiles --oplogSize 100 --fork

echo Executando Config Server
mongod --configsvr --dbpath /home/andre/data/cfg --logpath /home/andre/data/cfg.log --fork

echo Executando Mongos
mongos --configdb localhost:27019 --logpath /home/andre/data/mongos.log --fork

echo Nós em execução:
ps aux | grep mongod

