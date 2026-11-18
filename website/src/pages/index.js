import React from 'react';
import classnames from 'classnames';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBrokenLinks from '@docusaurus/useBrokenLinks';
import styles from './styles.module.css';
import FeaturesTwo from '../components/featuresTwo';
import { featuresTwo } from '../components/featuresTwo-config';
import FeaturesTwoHeader from '../components/featuresTwoHeader';
import { featuresTwoHeader } from '../components/featuresTwoHeader-config';
import FeaturesThree from '../components/featuresThree';
import { featuresThree, contributingGuideUrl } from '../components/featuresThree-config';

function Home() {
  const context = useDocusaurusContext();
  const {siteConfig = {}} = context;
  // The link checker only knows ids from Docusaurus headings; register the section's own.
  useBrokenLinks().collectAnchor('contribute');
  return (
    <Layout
      title={`${siteConfig.title}`}
      description={`${siteConfig.tagline}`}>
      <header className={classnames('hero hero--primary', styles.heroBanner)}>
        <div className="container">
          <img className={styles.featureImage} src='img/cdm-logo/Horizontal/2022_CDM_Horizontal_WHT.svg' alt='Common Domain Model Logo' />
          <div className={styles.buttons}>
            <Link
              className={classnames(
                'button button--outline button--secondary button--lg',
                styles.getStarted
              )}
              to={'/docs/training'}>
              Introduction Training
           </Link>     
           <Link
              className={classnames(
                'button button--outline button--secondary button--lg',
                styles.getStarted
              )}
              to={'docs/home'}>
              DOCS
            </Link>
            <Link
              className={classnames(
                'button button--outline button--secondary button--lg',
                styles.getStarted
              )}
              to={'https://github.com/finos/common-domain-model'}>
              GITHUB
            </Link>
            <Link
              className={classnames(
                'button button--outline button--secondary button--lg',
                styles.getStarted
              )}
              to={'#contribute'}>
              CONTRIBUTE
            </Link>
          </div>
        </div>
      </header>
      <main>
        {featuresTwoHeader && featuresTwoHeader.length && (
                <section className={styles.features}>
                  <div className="container">
                      {featuresTwoHeader.map((props, idx) => (
                        <FeaturesTwoHeader key={idx} {...props} />
                      ))}

                  </div>
                </section>
              )}
        {featuresTwo && featuresTwo.length && (
          <section className={styles.members}>
            <div className="container">
              <div className="row row--center">
              <h2>WHAT IS THE PURPOSE OF THE CDM?</h2>
              </div>
              <div className="row">
                {featuresTwo.map((props, idx) => (
                  <FeaturesTwo key={idx} {...props} />
                ))}
              </div>
            </div>
          </section>
        )}
        <section id="contribute" className={styles.projects}>
          <div className="container">
            <FeaturesTwoHeader
              title={<>HOW TO CONTRIBUTE</>}
              description={
                <span style={{ fontSize: '1.25em' }}>Anyone can contribute to the CDM, and most first contributions fix the documentation or this website.</span>
              }
            />
            <div className="row">
              {featuresThree.map((props, idx) => (
                <FeaturesThree key={idx} {...props} />
              ))}
            </div>
            <div className={styles.buttons}>
              <Link className="button button--primary button--lg" to={contributingGuideUrl}>
                Read the new contributor quickstart
              </Link>
            </div>
          </div>
        </section>
      </main>
    </Layout>
  );
}

export default Home;
