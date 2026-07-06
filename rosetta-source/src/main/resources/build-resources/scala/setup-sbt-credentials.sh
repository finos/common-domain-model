# Sets up sbt/Coursier authentication against the Google Artifact Registry Maven proxy.
# Intended to be sourced (not executed) from the CI pipeline, so the exported
# environment variables remain in effect for the subsequent `sbt` invocation, e.g.:
#   source ./setup-sbt-credentials.sh
#
# Multiple credential mechanisms are configured below because the sbt launcher's boot-level
# dependency resolution uses a different mechanism from the regular project-level Coursier
# resolution - see the comments in build.sbt for the corresponding sbt-side settings.
#
# IMPORTANT: the realm is discovered dynamically from the server's own WWW-Authenticate
# header rather than hardcoded, because Coursier/sbt only send credentials when the
# realm matches EXACTLY. `curl` ignores realm entirely, which is why a `curl` auth check
# can succeed while sbt/Coursier still fail with "unauthorized" if the realm is wrong.

# Version-agnostic URL used solely to trigger a 401 response and read its WWW-Authenticate header.
PROBE_URL="https://europe-west1-maven.pkg.dev/production-208613/maven-central/org/scala-lang/scala-library/maven-metadata.xml"

AR_REALM="$(curl --silent --show-error --location -D - -o /dev/null "$PROBE_URL" \
  | grep -i '^www-authenticate:' | sed -E 's/.*realm="([^"]*)".*/\1/i' | tr -d '\r\n')"

SA_KEY_FLAT="$(printf '%s' "${ARTIFACT_REGISTRY_SA_KEY}" | tr -d '\n\r')"

mkdir -p ~/.sbt ~/.config/coursier

printf 'realm=%s\nhost=europe-west1-maven.pkg.dev\nuser=_json_key_base64\npassword=%s\n' \
  "$AR_REALM" "$SA_KEY_FLAT" > ~/.sbt/.credentials

printf 'artifactregistry.username=_json_key_base64\nartifactregistry.password=%s\nartifactregistry.host=europe-west1-maven.pkg.dev\nartifactregistry.realm=%s\n' \
  "$SA_KEY_FLAT" "$AR_REALM" > ~/.config/coursier/credentials.properties

export COURSIER_CREDENTIALS="europe-west1-maven.pkg.dev(${AR_REALM}) _json_key_base64:$SA_KEY_FLAT"
export SBT_CREDENTIALS="$HOME/.sbt/.credentials"
