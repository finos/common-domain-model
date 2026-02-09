const React = require('react');
import Layout from "@theme/Layout";
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
const { versions } = require('../../scripts/schema-versions');

const SCHEMA_VERSIONS = versions.map(v => ({
  version: v.version,
  path: v.urlPath,
  status: v.status
}));

const styles = {
  container: {
    maxWidth: '1140px',
    margin: '0 auto',
    padding: '2rem',
  },
  post: {
    marginBottom: '2rem',
  },
  postHeader: {
    marginBottom: '2rem',
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
    marginBottom: '3rem',
  },
  th: {
    textAlign: 'left',
    padding: '0.75rem',
    borderBottom: '2px solid #ddd',
    fontWeight: 'bold',
  },
  td: {
    padding: '0.75rem',
    borderBottom: '1px solid #ddd',
  },
  badge: {
    display: 'inline-block',
    padding: '0.25rem 0.5rem',
    borderRadius: '3px',
    fontSize: '0.875rem',
    fontWeight: 'bold',
    backgroundColor: '#28a745',
    color: 'white',
  },
  link: {
    color: '#007bff',
    textDecoration: 'none',
  },
  description: {
    marginBottom: '2rem',
    fontSize: '1.1rem',
    color: '#666',
  },
};

export default (props) => {
  const context = useDocusaurusContext();
  const siteConfig = context.siteConfig;
  const repoUrl = siteConfig.customFields.repoUrl;

  return (
    <Layout title="CDM JSON Schemas">
      <div style={styles.container}>
        <div style={styles.post}>
          <header style={styles.postHeader}>
            <h1>CDM JSON Schemas</h1>
          </header>
          
          <p style={styles.description}>
            This page provides JSON Schema definitions for the Common Domain Model (CDM). These schemas provide 
            machine-readable validation rules and documentation for CDM data structures.
          </p>

          <h3>Available Schema Versions</h3>
          
          <table style={styles.table}>
            <thead>
              <tr>
                <th style={styles.th}>Version</th>
                <th style={styles.th}>Status</th>
                <th style={styles.th}>Schemas</th>
                <th style={styles.th}>Release Notes</th>
              </tr>
            </thead>
            <tbody>
              {SCHEMA_VERSIONS.map(({ version, path, status }) => (
                <tr key={version}>
                  <td style={styles.td}>
                    <strong>{version}</strong>
                    {status === 'Latest' && (
                      <span style={{ ...styles.badge, marginLeft: '0.5rem' }}>
                        {status}
                      </span>
                    )}
                  </td>
                  <td style={styles.td}>{status}</td>
                  <td style={styles.td}>
                    <a 
                      href={`/schemas/${path}/`} 
                      style={styles.link}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      Browse Schemas
                    </a>
                  </td>
                  <td style={styles.td}>
                    <a 
                      href={`${repoUrl}/releases/tag/${version}`}
                      style={styles.link}
                      target="_blank"
                      rel="noopener noreferrer"
                    >
                      Release Notes
                    </a>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <h3>Using the Schemas</h3>
          <p>
            CDM JSON schemas can be used to validate CDM instances programmatically. 
            Each schema is accessible via URL:
          </p>
          <pre style={{ 
            backgroundColor: '#f5f5f5', 
            padding: '1rem', 
            borderRadius: '4px',
            overflow: 'auto' 
          }}>
            {`https://cdm.finos.org/schemas/{version}/{schema-name}.schema.json`}
          </pre>
          
          <p>Example:</p>
          <pre style={{ 
            backgroundColor: '#f5f5f5', 
            padding: '1rem', 
            borderRadius: '4px',
            overflow: 'auto' 
          }}>
            {`https://cdm.finos.org/schemas/6.0/cdm-base-datetime-AdjustableDate.schema.json`}
          </pre>

          <h3>Schema Format</h3>
          <p>
            All schemas are provided in JSON Schema format (draft-07 compatible) and can be used 
            with standard JSON Schema validation tools and libraries.
          </p>

          <h3>More Information</h3>
          <p>
            For more details about the Common Domain Model, visit the{' '}
            <a href="/docs/home" style={styles.link}>documentation</a> or the{' '}
            <a href={repoUrl} style={styles.link} target="_blank" rel="noopener noreferrer">
              GitHub repository
            </a>.
          </p>
        </div>
      </div>
    </Layout>
  );
};
