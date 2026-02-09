#!/usr/bin/env node

/**
 * Generates index.html files for each schema version directory
 * Lists all available schemas with direct download links
 */

const fs = require('fs');
const path = require('path');
const { versions } = require('./schema-versions');

const SCHEMAS_DIR = path.join(__dirname, '..', 'static', 'schemas');
const VERSIONS = versions.map(v => v.urlPath);

function generateIndexHtml(version, schemas) {
  const schemaLinks = schemas.map(schema => 
    `        <li><a href="${schema}" download>${schema}</a></li>`
  ).join('\n');

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CDM ${version} JSON Schemas</title>
  <style>
    body {
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
      max-width: 1200px;
      margin: 0 auto;
      padding: 2rem;
      line-height: 1.6;
      color: #333;
    }
    h1 {
      color: #1a1a1a;
      border-bottom: 3px solid #007bff;
      padding-bottom: 0.5rem;
    }
    .info {
      background-color: #e7f3ff;
      border-left: 4px solid #007bff;
      padding: 1rem;
      margin: 1.5rem 0;
    }
    .schema-list {
      list-style: none;
      padding: 0;
    }
    .schema-list li {
      padding: 0.5rem;
      border-bottom: 1px solid #eee;
    }
    .schema-list li:hover {
      background-color: #f8f9fa;
    }
    .schema-list a {
      color: #007bff;
      text-decoration: none;
      font-family: 'Monaco', 'Courier New', monospace;
      font-size: 0.9rem;
    }
    .schema-list a:hover {
      text-decoration: underline;
    }
    .count {
      color: #666;
      font-size: 0.9rem;
      margin: 1rem 0;
    }
    .back-link {
      display: inline-block;
      margin-bottom: 1rem;
      color: #007bff;
      text-decoration: none;
    }
    .back-link:hover {
      text-decoration: underline;
    }
    .search-box {
      width: 100%;
      padding: 0.75rem;
      margin: 1rem 0;
      font-size: 1rem;
      border: 2px solid #ddd;
      border-radius: 4px;
    }
    .search-box:focus {
      outline: none;
      border-color: #007bff;
    }
  </style>
</head>
<body>
  <a href="/schemas" class="back-link">← Back to all versions</a>
  <h1>CDM ${version} JSON Schemas</h1>
  
  <div class="info">
    <strong>Version:</strong> ${version}<br>
    <strong>Total Schemas:</strong> ${schemas.length}<br>
    <strong>Format:</strong> JSON Schema (Draft 07)
  </div>

  <input 
    type="text" 
    class="search-box" 
    id="searchBox" 
    placeholder="Search schemas by name..."
    onkeyup="filterSchemas()"
  >

  <p class="count">Showing <span id="count">${schemas.length}</span> of ${schemas.length} schemas</p>

  <ul class="schema-list" id="schemaList">
${schemaLinks}
  </ul>

  <script>
    function filterSchemas() {
      const searchTerm = document.getElementById('searchBox').value.toLowerCase();
      const list = document.getElementById('schemaList');
      const items = list.getElementsByTagName('li');
      let visibleCount = 0;

      for (let item of items) {
        const text = item.textContent.toLowerCase();
        if (text.includes(searchTerm)) {
          item.style.display = '';
          visibleCount++;
        } else {
          item.style.display = 'none';
        }
      }

      document.getElementById('count').textContent = visibleCount;
    }
  </script>
</body>
</html>`;
}

// Main
console.log('Generating schema index pages...\n');

for (const version of VERSIONS) {
  const versionDir = path.join(SCHEMAS_DIR, version);
  
  if (!fs.existsSync(versionDir)) {
    console.log(`⚠ Skipping ${version} - directory not found`);
    continue;
  }

  const schemas = fs.readdirSync(versionDir)
    .filter(f => f.endsWith('.schema.json'))
    .sort();

  const html = generateIndexHtml(version, schemas);
  const indexPath = path.join(versionDir, 'index.html');
  
  fs.writeFileSync(indexPath, html);
  console.log(`✓ Generated index for ${version} (${schemas.length} schemas)`);
}

console.log('\n✓ Done!');
