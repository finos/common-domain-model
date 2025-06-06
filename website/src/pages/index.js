import React from 'react';
import classnames from 'classnames';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import styles from './styles.module.css';
import FeaturesTwo from '../components/featuresTwo';
import { featuresTwo } from '../components/featuresTwo-config';
import FeaturesTwoHeader from '../components/featuresTwoHeader';
import { featuresTwoHeader } from '../components/featuresTwoHeader-config';

function Home() {
  const context = useDocusaurusContext();
  const {siteConfig = {}} = context;
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
      </main>
    </Layout>
  );
}

export default Home;
