import React from 'react';
import classnames from 'classnames';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from '../pages/styles.module.css';

export default function FeaturesTwoHeader({title, description}) {
    return (
        <div className={classnames('text--center padding', styles.feature)}>
            <h2>{title}</h2>
            <div className={classnames(styles.featuresCTA)}>

                   {description}

            </div>
        </div>
    );
}