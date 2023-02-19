<h2>"Working with databases" - final project of 4 modules</h2>

1. Launch Mysql and Redis database containers in docker
We use the commands:

Redis: docker run -d --name redis -p 6379:6379 redis:latest 

Docker: docker run --name mysql -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root --restart unless-stopped -v mysql:/var/lib/mysql mysql:8

2. We compare the results obtained

![Movavi ScreenShot 070 -](https://user-images.githubusercontent.com/91777043/218772225-792b1ebb-2a02-4781-94f0-13c5ec208b5f.jpg)


<h2>Redis works faster by 24%</h2>
