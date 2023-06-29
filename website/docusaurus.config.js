// Docs at https://v2.docusaurus.io/docs/configuration


// Replace 'project-blueprint' with {project name}
const projectName = 'Common Domain Model'
// Replace 'project-blueprint' with {project name}
const projectSlug = 'project-blueprint'
// Replace 'FINOS' with {name of copyright owner}
const copyrightOwner = 'FINOS'

module.exports = {
  title: `${projectName}`,
  url: 'https://www.cdm.finos.org',
  baseUrl: '/',
  favicon: 'img/favicon/favicon.ico',
  projectName: `${projectName}`,
  organizationName: 'FINOS',
  customFields: {
    repoUrl: `https://github.com/finos/${projectSlug}`,
  },
  scripts: ['https://buttons.github.io/buttons.js'],
  stylesheets: ['https://fonts.googleapis.com/css?family=Overpass:400,400i,700'],
  themeConfig: {
    navbar: {
      title: `${projectName}`,
      logo: {
        alt: 'CDM Logo',
        src: 'img/cdm-logo/Icon/2022_CDM_Icon_WHT.png',
      },
      items: [
        {to: 'docs/home', label: 'Docs', position: 'right'},
        {
          href: 'https://github.com/finos/common-domain-model',
          label: 'GitHub',
          position: 'right',
        }
      ],
    },
    footer: {
      copyright: `Copyright © ${new Date().getFullYear()} ${projectName} - ${copyrightOwner}`,
      logo: {
        alt: 'CDM Logo',
        src: 'img/cdm-logo/Icon/2022_CDM_Icon_WHT.png',
        href: 'https://www.finos.org/common-domain-model'
      },
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Getting Started',
              to: 'docs/home',
            },
            {
              label: 'About FINOS',
              to: 'docs/about-finos',
            }
          ]
        },
      ]
    },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          path: '../docs',
          editUrl:
            'https://github.com/finos/open-developer-platform/edit/master/website/',
          sidebarPath: require.resolve('./sidebars.js')
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        }
      }
    ]
  ]
};
