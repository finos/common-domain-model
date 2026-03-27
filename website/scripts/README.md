# CDM Schema Management

This directory contains scripts for managing CDM JSON schemas for the documentation website.

## Scripts

- **`schema-versions.js`** - Centralized configuration for schema versions
- **`download-schemas.js`** - Downloads schemas from Maven Central
- **`generate-schema-indexes.js`** - Creates index.html files for each version

## Adding a New Schema Version

When a new CDM version is released, update **only** `schema-versions.js`:

```javascript
module.exports = {
  versions: [
    { version: '7.0.0', urlPath: '7.0', status: 'Latest' },      // New version
    { version: '6.0.0', urlPath: '6.0', status: 'Previous' },    // Update status
    { version: '5.20.0', urlPath: '5.20', status: 'Previous' },
    { version: '5.13.0', urlPath: '5.13', status: 'Previous' },
  ],
  // ... rest of config
};
```

Then run:

```bash
npm run download-schemas
```

This will:
1. Download the new schema version from Maven Central
2. Generate the index.html page for browsing schemas
3. Update the schemas page to show the new version

## Version Configuration

Each version object has:
- **`version`** - Maven artifact version (e.g., '6.0.0')
- **`urlPath`** - URL path for schemas (e.g., '6.0' → `/schemas/6.0/`)
- **`status`** - Display status on the schemas page ('Latest', 'Previous', etc.)

## URL Structure

Schemas are accessible at:
- Landing page: `https://cdm.finos.org/schemas`
- Version index: `https://cdm.finos.org/schemas/{urlPath}/`
- Individual schema: `https://cdm.finos.org/schemas/{urlPath}/{schema-name}.schema.json`

## Automatic Updates

Schemas are automatically downloaded before each build via the `prebuild` script in `package.json`.
