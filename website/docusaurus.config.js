// Docs at https://v2.docusaurus.io/docs/configuration


// Replace 'project-blueprint' with {project name}
const projectName = 'Common Domain Model'
// Replace 'project-blueprint' with {project name}
const projectSlug = 'common-domain-model'
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
        {
          title: 'CDM Working Groups',
          items: [
            {
              label: 'Contribution Review Working Group',
              to: '...',
            },
            {
              label: 'Collateral Working Group',
              to: '...',
            },
            {
              label: 'Technology Architecture Working Group',
              to: '...',
            },
            {
              label: 'Structured Products Working Group',
              to: '...',
            }
          ]
        },
        {
          title: 'More CDM Working Groups',
          items: [
            {
              label: 'FINOS CDM Steering Working Group',
              to: '...',
            },
            {
              label: 'ISLA CDM Working Group (Securities Lending)',
              to: 'https://www.islaemea.org/working-groups/',
            },
            {
              label: 'ISLA CDM Trading Working Group (Securities Lending)',
              to: 'https://www.islaemea.org/working-groups/',
            },
            {
              label: 'ISLA Document Digitisation Working Group (GMSLA)',
              to: 'https://www.islaemea.org/working-groups/',
            },
          ]
        },
        {
          title: 'Consume',
          items: [
            {
              label: 'Releases',
              to: 'https://github.com/finos/common-domain-model/releases',
            },
            {
              label: 'FAQ',
              to: 'https://github.com/finos/common-domain-model/issues/2058',
            },
            {
              label: 'CDM Java Distribution Guidelines',
              to: 'http://localhost:3001/docs/cdm-guidelines',
            },
            {
              label: 'Download Source Code',
              to: 'http://localhost:3001/docs/download',
            }
          ]
        },
        {
          title: 'FINOS',
          items: [
            {
              label: 'FINOS Website',
              to: 'https://www.finos.org',
            },
            {
              label: 'FINOS GitHub Org',
              to: 'github.com/finos',
            },
            {
              label: 'CDM GitHub Repo',
              to: 'https://www.finos.org/common-domain-model',
            },
            {
              label: 'CDM Launch Press Release',
              to: 'https://www.finos.org/press/finos-launches-common-domain-model-project-in-partnership-with-isda-isla-and-icma',
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
            'https://github.com/finos/common-domain-model/edit/master/website/',
          sidebarPath: require.resolve('./sidebars.js')
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        }
      }
    ]
  ]
};
