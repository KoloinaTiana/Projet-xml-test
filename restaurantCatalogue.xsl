<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:template match="/">
    <html>
      <head>
        <title>Restaurant Catalogue</title>
      </head>
      <body>
        <h1>Menu</h1>
        <table>
          <tr>
            <th>Nom</th>
            <th>Description</th>
            <th>Prix</th>
            <th>Catégorie</th>
            <th>Ingrédients</th>
            <th>Image</th>
          </tr>
          <xsl:for-each select="restaurantCatalogue/plat">
            <tr>
              <td><xsl:value-of select="nom"/></td>
              <td><xsl:value-of select="description"/></td>
              <td><xsl:value-of select="prix"/></td>
              <td><xsl:value-of select="categorie"/></td>
              <td>
                <xsl:for-each select="ingredients/ingredient">
                  <xsl:value-of select="."/><xsl:if test="position()!=last()">, </xsl:if>
                </xsl:for-each>
              </td>
              <td><img src="{image}" alt="{nom}" height="100px" width="100px"/></td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>