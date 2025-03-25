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
        {to: 'docs/cdm-materials', label: 'CDM Resources', position: 'right'},
        {to: 'docs/get-involved', label: 'Get Involved', position: 'right'},
        {
          href: 'https://github.com/finos/common-domain-model',
          label: 'GitHub',
          position: 'right',
        },
        {
          type: 'docsVersionDropdown',
          position: 'left',
          dropdownActiveClassDisabled: true,
        },
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
              href: 'https://cdm.finos.org/docs/CDM-Contribution-Review-WG',
            },
            {
              label: 'Collateral Working Group',
              href: 'https://cdm.finos.org/docs/CDM-Collateral-WG',
            },
            {
              label: 'Derivatives Working Group',
              href: 'https://cdm.finos.org/docs/CDM-Derivatives-WG',
            },
            {
              label: 'Technology Architecture Working Group',
              href: 'https://cdm.finos.org/docs/CDM-Technology-Architecture-WG',
            },
            {
              label: 'Structured Products Working Group',
              href: 'https://cdm.finos.org/docs/CDM-Structured-Products-WG',
            }
          ]
        },
        {
          title: 'More CDM Working Groups',
          items: [
            {
              label: 'FINOS CDM Steering Working Group',
              href: 'https://cdm.finos.org/docs/CDM-Steering-WG',
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
              to: 'docs/cdm-java-distribution',
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
        },
        gtag: {
          trackingID: "G-9ZYPYF37E7",
          anonymizeIP: true,
        }
      }
    ]
  ]
};
