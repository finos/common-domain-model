#!/usr/bin/env python3

# Configuration file for the Sphinx documentation builder.
#
# This file only contains a selection of the most common options. For a full
# list see the documentation:
# http://www.sphinx-doc.org/en/master/config

# -- Path setup --------------------------------------------------------------

# If extensions (or modules to document with autodoc) are in another directory,
# add these directories to sys.path here. If the directory is relative to the
# documentation root, use os.path.abspath to make it absolute, like shown here.
#
# import os
# import sys
# sys.path.insert(0, os.path.abspath('.'))


# -- Project information -----------------------------------------------------

project = 'Documentation'
copyright = '2022, ISDA'
author = 'REGnosys'

# The short X.Y version.
#version = 'X.Y.Z'
# The full version, including alpha/beta/rc tags.
#release = 'X.Y.Z'

# -- General configuration ---------------------------------------------------

# Add any Sphinx extension module names here, as strings. They can be
# extensions coming with Sphinx (named 'sphinx.ext.*') or your custom
# ones.
extensions = ['myst_parser']

# The suffix(es) of source filenames.
# You can specify multiple suffix as a list of string:
#
# source_suffix = ['.rst', '.md']
source_suffix = ['.rst', '.md']

# The master toctree document.
master_doc = 'index'

# Add any paths that contain templates here, relative to this directory.
templates_path = ['_templates']

# List of patterns, relative to source directory, that match files and
# directories to ignore when looking for source files.
# This pattern also affects html_static_path and html_extra_path.
exclude_patterns = ['_build', 'Thumbs.db', '.DS_Store']

# The name of the Pygments (syntax highlighting) style to use.
pygments_style = 'sphinx'

# If true, `todo` and `todoList` produce output, else they produce nothing.
todo_include_todos = True


# -- Options for HTML output -------------------------------------------------

# The theme to use for HTML and HTML Help pages.  See the documentation for
# a list of builtin themes.
#
# html_theme = 'sphinx_rtd_theme'

html_theme = "sphinx_material"
html_favicon = 'favicon.ico'
# Material theme options (see theme.conf for more information)
html_theme_options = {

    # Set the name of the project to appear in the navigation.
    'nav_title': 'CDM Documentation',

    'color_primary': 'blue',
    'color_accent': 'light-blue',

    "html_minify": False,
    "html_prettify": False,
    "css_minify": True,
    # "logo_icon": "&#xe869",
    # "repo_type": "github",
    "globaltoc_depth": 2,
    "color_primary": "blue",
    "color_accent": "cyan",
    # "touch_icon": "images/apple-icon-152x152.png",
    "theme_color": "#2196f3",
    "master_doc": False,
    # "nav_links": [
    #     {"href": "index", "internal": True, "title": "Material"},
    #     {
    #         "href": "https://squidfunk.github.io/mkdocs-material/",
    #         "internal": False,
    #         "title": "Material for MkDocs",
    #     },
    # ],
    # "logo": "https://cdn.aws.isda.org/a/fGWTE/CDM-hoz-logo-print.png",

    "version_dropdown": True,
    # "version_json": "_static/versions.json",
    # "version_info": {
        # "Release": "https://bashtage.github.io/sphinx-material/",
        # "Development": "https://bashtage.github.io/sphinx-material/devel/",
        # "Release (rel)": "/sphinx-material/",
        # "Development (rel)": "/sphinx-material/devel/",
    # },
    "table_classes": ["plain"],


}

html_sidebars = {
    "**": ["logo-text.html", "globaltoc.html", "localtoc.html", "searchbox.html"]
}


### html_theme_path is relative to this file...
# if you are on your Mac it should be brewed here: ['../usr/local/lib/python3.7/dist-packages/']
html_theme_path = ['../../../usr/local/lib/python3.7/site-packages']

## uncomment when javadoc is to be ready
## html_extra_path = ['javadoc']

# Add any paths that contain custom static files (such as style sheets) here,
# relative to this directory. They are copied after the builtin static files,
# so a file named "default.css" will overwrite the builtin "default.css".

html_static_path = ['_static']

# html_css_files = [
#     'css/custom.css',
# ]

# html_style = 'css/override.css'
