mkdir /home/andre/data/
mkdir /home/andre/data/r1s1
mkdir /home/andre/data/r2s1
mkdir /home/andre/data/r3s1
echo Executando Mongod - Nó 1
mongod --port 27001 --replSet s1 --dbpath /home/andre/data/r1s1 --logpath /home/andre/data/r1s1.log --smallfiles --oplogSize 100 --fork
echo Executando Mongod - Nó 2
mongod --port 27002 --replSet s1 --dbpath /home/andre/data/r2s1 --logpath /home/andre/data/r2s1.log --smallfiles --oplogSize 100 --fork
echo Executando Mongod - Nó 3
mongod --port 27003 --replSet s1 --dbpath /home/andre/data/r3s1 --logpath /home/andre/data/r3s1.log --smallfiles --oplogSize 100 --fork
echo Nós em execução:
ps aux | grep mongod

