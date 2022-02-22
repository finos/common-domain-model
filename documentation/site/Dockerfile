FROM regnosys-docker-registry.jfrog.io/regnosys/sphinx:latest

RUN pip3 install sphinx-material

RUN mkdir /docs_source /docs_out

## copy configured conf.py and master doc in there
COPY conf.py  /docs_source
COPY index.rst  /docs_source
COPY _templates/  /docs_source/_templates/
COPY _static/  /docs_source/_static/

## copy the rest .rst files and any other files in there
COPY cdm/  /docs_source/cdm/
COPY cdm/documentation/source/code-snippets/ /docs_source/cdm/documentation/source/code-snippets/
COPY dsl/  /docs_source/dsl/
COPY core/  /docs_source/core/

RUN ls -la /docs_source/*

# use when javadoc is ready
# COPY javadoc  /docs_source/javadoc

## generate documentation
RUN /usr/bin/sphinx-build -n -b html /docs_source  /docs_out

## copy output to serve dir
RUN cp -r /docs_out/* /usr/share/nginx/html
