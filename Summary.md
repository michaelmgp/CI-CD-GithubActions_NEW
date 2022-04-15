# CI/CD with Github Action
## Pada Section Ini Terdapa beberapa Hal yang saya dapat 

## 1. CI/CD
Adalah suatu konsep untuk melakukan integrasi suatu aplikasi dan deployment secara otomatis ci/cd ini memiliki pipeline mulai dari.

### Continuous integration 
- BUILD
- TEST
- MERGE

### Continuous Delivery
- Automatically Release to Repository

### Continuous Deployment
- Automatically Deploy to Production

## 2. Github ACtion
Sebuah fitur pada Github yang memungkin kita untuk membuat suatu workflow untuk melakukan CI/CD
```yml
name: Java CI with Maven

on:
  push:
    branches: [ master ]


jobs:
  run_test:
    name: Unit Test
    runs-on: ubuntu-18.04
    steps:
      - run: echo "Starting execute unit test"
      - uses: actions/checkout@v3
      - name: Setup JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: 11
          distribution: 'adopt'
      - name: Maven Verify
        run: mvn clean verify


  build:
    name: Build
    runs-on: ubuntu-18.04
    needs: run_test
    steps:
      - run: echo "Starting execute unit test"
      - uses: actions/checkout@v3
      - name: Setup JDK 11
        uses: actions/setup-java@v3
        with:
          java-version: 11
          distribution: 'adopt'
      - name: Maven Build
        run: mvn clean package -Dmaven.test.skip=true
      - name: Login to DockerHub
        uses: docker/login-action@v1
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}
      - name: Build and push
        uses: docker/build-push-action@v2
        with:
          context: .
          push: true
          tags: ${{ secrets.DOCKERHUB_USERNAME }}/example-cicd:latest
  deployment:
    name: Deployment
    runs-on: ubuntu-18.04
    needs: build
    steps:
      - uses: actions/checkout@v3
      - name: copy file via ssh key
        uses: appleboy/scp-action@master
        with:
          host: ${{ secrets.SSH_HOST }}
          username: ${{ secrets.SSH_USER }}
          port: 22
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          source: "./dev.env"
          target: /home/${{ secrets.SSH_USER }}
      - name: Deploy Using ssh
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.SSH_HOST }}
          username: ${{ secrets.SSH_USER }}
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          port: 22
          script: |
            docker stop example-cicd
            docker rmi michaelmgp/example-cicd:latest
            docker pull michaelmgp/example-cicd:latest
            docker start postgres
            docker run -d --rm --name example-cicd -p 80:8080 --env-file=dev.env --network my_network michaelmgp/example-cicd:latest

```

## 3. Cloud Platform
Adalah suatu soerver cloud sebagai tempat aplikasi kita dideploy. Pada kesempatan ini saya menggunakan google cloud. untuk melaukan deployment dibutuhkan docker untuk menset-up evironment dari Aplikasi kita. disini saya juga mengkoneksikan postgresql sebagai database pada aplikasi backend saya