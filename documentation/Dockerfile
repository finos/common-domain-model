FROM regnosys-docker-registry.jfrog.io/regnosys/sphinx:latest

RUN pip3 install sphinx-material
RUN pip3 install --upgrade myst-parser
RUN apt-get install jq -y

RUN mkdir /docs_source /docs_out /documentation
COPY . /documentation
WORKDIR /documentation

RUN ./build.sh

## generate documentation
RUN /usr/bin/sphinx-build -n -b html build /docs_out

FROM nginx:1.21

## copy output to serve dir
COPY --from=0 /docs_out /usr/share/nginx/html
