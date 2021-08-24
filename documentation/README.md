# Prerequisites
The documentation is written using reStructuredText markup language and built into static html pages using Sphyinx.  Sphyinx requires at least Python 2.7 or 3.4.

## Brew
Check brew is installed
> brew --version

If not
> /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

## Python
> python3 --version

If not
> brew install python3

## Sphinx
> pip3 install sphinx

# Writing reStructuredText
Atom editor has a nice interface and an extension that renders reStructuredText in a live preview.

Get a free copy of Atom from https://atom.io/

> Packages > Settings View > Install Packages > Search: rst-preview

## Building Documentation
Building the documentation has been tied into the maven build.  Run the below

> mvn clean install

And take a look at

> docs/build/html/index.html
