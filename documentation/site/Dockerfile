FROM regnosys-docker-registry.jfrog.io/regnosys/sphinx:latest

RUN pip3 install sphinx-material
RUN pip3 install --upgrade myst-parser

RUN mkdir /docs_source /docs_out

## copy configured conf.py and master doc in there
COPY conf.py  /docs_source
COPY _templates/  /docs_source/_templates/
COPY _static/  /docs_source/_static/

## copy the rest .rst files and any other files in there
COPY build/  /docs_source/

RUN ls -la /docs_source/*

## generate documentation
RUN /usr/bin/sphinx-build -n -b html /docs_source  /docs_out

## copy output to serve dir
RUN cp -r /docs_out/* /usr/share/nginx/html
