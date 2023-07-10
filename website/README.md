This website was created with [Docusaurus v2](https://v2.docusaurus.io/).

In order to start working with Docusaurus, please read the [Getting Started guide](https://docusaurus.io/docs/configuration) and browse through the following folders and files:
- `website` - contains the Node/React code to build the website
- `website/docusaurus.config.js` - contains the Docusaurus configuration; you'll need to edit this file.
- `website/static` - contains images, PDF and other static assets used in the website; if you add a `file.pdf` in this folder, it will be served as `https://<your_host>/file.pdf`.
- `docs` - contains the `.md` and `.mdx` files that are served as `https://<your_host>/<file_id>` ; the `file_id` is defined at the top of the file.

## Local run

Running Docusaurus locally is very simple, just follow these steps:
- Make sure `node` version is 14 or higher, using `node -v` ; you can use [nvm](https://github.com/nvm-sh/nvm) to install different node versions in your system.
- `cd website ; npm install ; npm run start`

The command should open your browser and point to `http://localhost:3000`.

## Deployment

[Netlify] (https://www.netlify.com/) is the default way to serve FINOS websites publicly. Find docs [here] (https://docs.netlify.com/configure-builds/get-started/).

You can configure Netlify using your own GitHub account, pointing to a personal repository (or fork); when adding a new site, please use the following configuration:
- Woeking directory: `website`
- Build command: `yarn build`
- Build directory: `website/build`

If you want to serve your website through `https://<project_name>.finos.org`, please email [help@finos.org](mailto:help@finos.org). To check a preview, visit https://project-blueprint.finos.org .
