import React from 'react';

export const contributingGuideUrl =
    'https://github.com/finos/common-domain-model/blob/master/CONTRIBUTING.md#new-contributor-quickstart';

export const featuresThree = [
    {
        title: <>CHECK YOUR CLA COVERAGE</>,
        imageUrl: '',
        description: (
            <>
            EasyCLA checks every commit. Sign as an individual, or ask your firm's CLA manager to add you, and enrol as a participant to join the working groups.{' '}
            <a href="https://github.com/finos/common-domain-model/blob/master/CONTRIBUTING.md#1-check-that-a-contributor-license-agreement-covers-you">How to get covered</a>
            </>
        ),
    },
    {
        title: <>START WITH THE DOCUMENTATION</>,
        imageUrl: '',
        description: (
            <>
            Fixing a typo, a broken link or an unclear sentence on this site needs one maintainer's approval and no working group review.{' '}
            <a href="https://github.com/finos/common-domain-model/blob/master/CONTRIBUTING.md#3-choose-a-first-contribution">Choose a first contribution</a>
            </>
        ),
    },
    {
        title: <>BRING MODEL CHANGES TO A WORKING GROUP</>,
        imageUrl: '',
        description: (
            <>
            Open an issue first. A working group approves it and assigns a maintainer to review your pull request.{' '}
            <a href="https://github.com/finos/common-domain-model/blob/master/CONTRIBUTING.md#7-ask-before-you-take-on-a-larger-change">Before you change the model</a>
            </>
        ),
    }
];
