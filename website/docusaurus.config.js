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
    colorMode: {
      defaultMode: 'light',
      disableSwitch: true
    },
    navbar: {
      title: `${projectName}`,
      logo: {
        alt: 'CDM Logo',
        src: 'img/cdm-logo/Icon/2022_CDM_Icon_WHT.png',
      },
      items: [
        {to: 'docs/home', label: 'Docs', position: 'right'},
        {to: 'docs/get-involved', label: 'Get Involved', position: 'right'},
        {to: '/pdfs/CDM-Overview.pptx.pdf', label: 'CDM Overview', position: 'right'},
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
          title: 'CDM Working Groups',
          items: [
            {
              label: 'Contribution Review Working Group',
              href: 'https://github.com/eteridvalishvili/common-domain-model/blob/master/website/static/pdfs/CDM.Contribution.Review.Working.Group.Terms.of.Reference.pdf',
            },
            {
              label: 'Collateral Working Group',
              href: 'https://github.com/eteridvalishvili/common-domain-model/blob/master/website/static/pdfs/CDM.Collateral.WG.Book.of.Work.May.2023.pdf',
            },
            {
              label: 'Technology Architecture Working Group',
              href: 'https://github.com/eteridvalishvili/common-domain-model/blob/master/website/static/pdfs/CDM.Technology.Architecture.WG.ToR.v1.pdf',
            },
            {
              label: 'Structured Products Working Group',
              href: 'https://github.com/finos/common-domain-model#active-working-groups',
            }
          ]
        },
        {
          title: 'More CDM Working Groups',
          items: [
            {
              label: 'FINOS CDM Steering Working Group',
              href: 'https://github.com/finos/common-domain-model#2-working-groups',
            },
            {
              label: 'ISLA CDM Working Group (Securities Lending)',
              href: 'https://www.islaemea.org/working-groups/',
            },
            {
              label: 'ISLA CDM Trading Working Group (Securities Lending)',
              href: 'https://www.islaemea.org/working-groups/',
            },
            {
              label: 'ISLA Document Digitisation Working Group (GMSLA)',
              href: 'https://www.islaemea.org/working-groups/',
            },
          ]
        },
        {
          title: 'Consume',
          items: [
            {
              label: 'CDM GitHub Repo',
              href: 'https://github.com/finos/common-domain-model',
            },
            {
              label: 'Releases',
              href: 'https://github.com/finos/common-domain-model/releases',
            },
            {
              label: 'What Is The CDM?',
              to: '/pdfs/Why-CDM.pdf',
              target: '_blank'
            },
            {
              label: 'Frequently Asked Questions',
              to: '/pdfs/FAQ-CDM.pdf',
              target: '_blank'
            },
            {
              label: 'CDM Java Distribution Guidelines',
              to: 'docs/cdm-guidelines',
            },
            {
              label: 'Download Source Code',
              to: 'docs/download',
            },
            {
              label: 'CDM Launch Press Release',
              href: 'https://www.finos.org/press/finos-launches-common-domain-model-project-in-partnership-with-isda-isla-and-icma',
            }
          ]
        },
        {
          title: 'FINOS',
          items: [
            {
              label: 'FINOS Website',
              href: 'https://www.finos.org',
            },
            {
              label: 'FINOS GitHub Org',
              href: 'https://www.github.com/finos',
            },
            {
              label: 'About FINOS',
              to: 'https://www.finos.org/about-us',
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
