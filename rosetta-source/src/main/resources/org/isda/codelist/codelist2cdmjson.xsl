<?xml version="1.0" encoding="UTF-8"?>

<!--Script to add the version information to the schema and to example files, and translate for view-specific overrides -->
<!-- Developed for ISDA by
Brian Lynn, Global Electronic Markets
Manuel Martos, TradeHeader -->
<xsl:stylesheet version="2.0"
                xmlns:gcl="http://xml.genericode.org/2004/ns/CodeList/0.2/"
                xmlns:doc="http://www.fpml.org/coding-scheme/documentation"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
    <xsl:output method="text" indent="yes"/>

    <!-- invoke the merge-scheme template to merge the many scheme files into a single output file -->
    <xsl:template match ="*"/>
    <xsl:template match ="/">
        <xsl:apply-templates mode="merge-scheme" select="."/>
    </xsl:template>

    <xsl:template mode="merge-scheme" match="*">
        <!-- convenience variables for retrieving data from the dataset -->
        <xsl:variable name="codes" select="//SimpleCodeList/Row/Value[1]/SimpleValue"/>
        <xsl:variable name="annotate" select="//*[local-name(.)='CodeList'][position()=last()]/Annotation"/>
        <xsl:variable name="id" select="//*[local-name(.)='CodeList'][position()=last()]/Identification"/>
        <xsl:variable name="latest" select="//*[local-name(.)='CodeList'][position()=last()]/SimpleCodeList"/>
        <!--<xsl:message>Latest has <xsl:value-of select="count($latest/Row)"/></xsl:message>-->
        <!-- output identification section (header) -->
        {
        "identification" : {
        "description" : "<xsl:apply-templates mode="clean" select="$annotate/Description/doc:definition"/>",
        "publicationDate" : "<xsl:value-of select="$annotate/Description/doc:publicationDate"/>",
        "shortName" : "<xsl:value-of select="$id/ShortName"/>",
        <!--"codeShortId" : "<xsl:value-of select="$id/ShortName"/>",-->
        "version" : "<xsl:value-of select="$id/Version"/>",
        "canonicalUri" : "<xsl:value-of select="$id/CanonicalUri"/>",
        "canonicalVersionUri" : "<xsl:value-of select="$id/CanonicalVersionUri"/>",
        "locationUri" : "<xsl:value-of select="$id/LocationUri"/>"
        },
        <!-- merge/output codes -->
        "codes" : [
        <xsl:for-each-group select="$codes" group-by=".">
            <xsl:sort select="." />
            <xsl:variable name="effectiveDate" select="($codes[.= current-grouping-key()]/../../../../Annotation/Description/doc:publicationDate)[1]"/>
            <xsl:variable name="in2021" select="($codes[.= current-grouping-key()]/../../Value[4]/ComplexValue/doc:definition/doc:reference[@vmeersion='2021'])"/>
            <xsl:variable name="code" select="."/>
            <!-- ensure present in the latest published version -->
            <xsl:if test="not($latest/Row/Value[1]/SimpleValue[.=$code])">
                <xsl:message> Skipped code <xsl:value-of select="."/>, not present in latest data set.  </xsl:message>
            </xsl:if>
            <xsl:if test="$latest/Row/Value[1]/SimpleValue[.=$code]">
                {
                "value" : "<xsl:value-of select="."/>",
                <xsl:if test="$effectiveDate">"effectiveDate" : "<xsl:value-of select="$effectiveDate"/>", </xsl:if>
                <!--<xsl:if test="($schemeName='floating-rate-index') and not($in2021)"> "deprecatedDate":  "2021-10-04", </xsl:if>-->
                "effectiveVersion" : "<xsl:value-of select="($codes[.= current-grouping-key()]/../../../../Identification/Version)[1]"/>",
                "source" : "<xsl:value-of select="($codes[.=current-grouping-key()]/../../Value[2]/SimpleValue)[last()]"/>",
                "description" : "<xsl:apply-templates mode="clean" select="($codes[.=current-grouping-key()]/../../Value[3]/SimpleValue)[last()]"/>"
                }<xsl:if test="position() != last()">,</xsl:if>
            </xsl:if>
        </xsl:for-each-group>
        ]
        }
    </xsl:template>

    <!-- map illegal characters -->
    <xsl:template mode="clean" match="*">
        <xsl:variable name="desc"><xsl:value-of select="."/></xsl:variable>
        <xsl:variable name="cleandesc" select="translate($desc, '\&quot;','_' )" />
        <xsl:variable name="cleandesc2" select="translate($cleandesc, '\&#09;\&#10;','' )" />
        <xsl:value-of select="$cleandesc2"/>
    </xsl:template>
</xsl:stylesheet>
