<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global template to match the root element -->
  <xsl:template match="/">

    <html>
      <head>
        <title>Menu du restaurant</title>
        <link rel="stylesheet" type="text/css" href="restaurantCatalogue.css"/>
      </head>

      <body>

        <h1>Menu du restaurant</h1>

        <xsl:for-each select="restaurantCatalogue/plat">
          <table>
            <tr>
              <th colspan="2">
                <xsl:value-of select="nom"/>
              </th>
            </tr>
            <tr>
              <td rowspan="{count(ingredients/ingredient)+3}">
                <img>
                  <xsl:attribute name="src">
                    <xsl:value-of select="image"/>
                  </xsl:attribute>
                </img>
              </td>
              <td>
                <xsl:value-of select="description"/>
              </td>
            </tr>
            <tr>
              <td class="categorie">
                <xsl:value-of select="categorie"/>
              </td>
            </tr>
            <tr>
              <td>
                <strong>Ingrédients :</strong>
              </td>
            </tr>
            <xsl:for-each select="ingredients/ingredient">
              <tr>
                <td>
                  <xsl:value-of select="."/>
                </td>
              </tr>
            </xsl:for-each>
            <tr>
              <td>
                <strong>Prix : </strong> <xsl:value-of select="prix"/> €
              </td>
            </tr>
          </table>
        </xsl:for-each>

      </body>
    </html>
    
  </xsl:template>
  
</xsl:stylesheet>