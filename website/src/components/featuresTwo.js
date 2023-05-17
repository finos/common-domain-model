import React from 'react';
import classnames from 'classnames';
import useBaseUrl from '@docusaurus/useBaseUrl';
import styles from '../pages/styles.module.css';

export default function FeaturesTwo({imageUrl, title, description}) {
    const imgUrl = useBaseUrl(imageUrl);
    return (
        <div className={classnames('text--center col col--4 padding', styles.feature)}>
            {imgUrl && (
                <div>
                <img className={styles.featureImage} src={imgUrl} alt={title} />
                </div>
            )}
            <h3>{title}</h3>
            <div className={classnames(styles.featuresCTA)}>
                <ul>
                    <li>{description}</li>
                </ul>
            </div>
        </div>
    );
}