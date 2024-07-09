<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
xmlns="http://purl.org/rss/1.0/"
xmlns:admin="http://webns.net/mvcb/"
xmlns:atom="http://www.w3.org/2005/Atom"
xmlns:atom10="http://www.w3.org/2005/Atom"
xmlns:c="http://s.opencalais.com/1/pred/"
xmlns:cc="http://web.resource.org/cc/"
xmlns:content="http://purl.org/rss/1.0/modules/content/"
xmlns:dc="http://purl.org/dc/elements/1.1/"
xmlns:dct="http://purl.org/dc/terms/"
xmlns:dbo="http://dbpedia.org/ontology/"
xmlns:dbp="http://dbpedia.org/property/"
xmlns:enc="http://purl.oclc.org/net/rss_2.0/enc#"
xmlns:fh="http://purl.org/syndication/history/1.0"
xmlns:foaf="http://xmlns.com/foaf/0.1/"
xmlns:foo="http://purl.org/rss/1.0/"
xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#"
xmlns:georss="http://www.georss.org/georss"
xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd"
xmlns:media="http://search.yahoo.com/mrss/"
xmlns:owl="http://www.w3.org/2002/07/owl#"
xmlns:prov="http://www.w3.org/ns/prov#"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
xmlns:sioc="http://rdfs.org/sioc/ns#"
xmlns:sioct="http://rdfs.org/sioc/types#"
xmlns:skos="http://www.w3.org/2004/02/skos/core#"
xmlns:slash="http://purl.org/rss/1.0/modules/slash/"
xmlns:syn="http://purl.org/rss/1.0/modules/syndication/"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output
    method = 'html'
    indent = 'yes'
    omit-xml-decleration='no' />
    
    <!-- Atom 1.0 Syndication Format -->
    <xsl:output
    media-type='application/atom+xml' />
    <xsl:template match='/atom:feed'>
        <!-- index right-to-left language codes -->
        <!-- TODO http://www.w3.org/TR/xpath/#function-lang -->
        <xsl:variable name='rtl'
        select='@xml:lang[
        contains(self::node(),"ar") or 
        contains(self::node(),"fa") or 
        contains(self::node(),"he") or 
        contains(self::node(),"ji") or 
        contains(self::node(),"ku") or 
        contains(self::node(),"ur") or 
        contains(self::node(),"yi")]'/>
        <html>
            <head>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"description"' />
                    <xsl:with-param name='content' select='atom:subtitle' />
                </xsl:call-template>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"generator"' />
                    <xsl:with-param name='content' select='"syndication4humans https://sjehuda.github.io/"' />
                </xsl:call-template>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"mimetype"' />
                    <xsl:with-param name='content' select='"application/atom+xml"' />
                </xsl:call-template>
                <title>
                    <xsl:choose>
                        <xsl:when test='atom:title and not(atom:title="")'>
                            <xsl:value-of select='atom:title'/>
                        </xsl:when>
                        <xsl:otherwise>Streamburner News Reader</xsl:otherwise>
                    </xsl:choose>
                </title>
                <!-- TODO media='print' -->
                <link href='streamburner.css' rel='stylesheet' type='text/css' media='screen'/>
                <!-- whether language code is of direction right-to-left -->
                <xsl:if test='$rtl'>
                    <link id='semitic' href='syndication-rtl.css' rel='stylesheet' type='text/css' />
                </xsl:if>
            </head>
            <body>
                <div id='feed'>
                    <header>
                        <!-- feed title -->
                        <div id='title'>
                            <xsl:choose>
                                <xsl:when test='atom:title and not(atom:title="")'>
                                    <xsl:attribute name='title'>
                                        <xsl:value-of select='atom:title'/>
                                    </xsl:attribute>
                                    <xsl:value-of select='atom:title'/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='empty'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                        </div>
                        <!-- feed subtitle -->
                        <div id='subtitle'>
                            <xsl:attribute name='title'>
                                <xsl:value-of select='atom:subtitle'/>
                            </xsl:attribute>
                            <xsl:value-of select='atom:subtitle'/>
                        </div>
                    </header>
                    <section id='links'>
                        <a title='Click to get the latest updates and news' onclick='location.href = "feed:" + location.href'>
                        </a>
                        <a title='Subscribe via SubToMe'>
                            <xsl:attribute name="href">
                                https://www.subtome.com/#/subscribe?feeds=<xsl:value-of select="atom:link[@rel='self']/@href" />
                            </xsl:attribute>
                            <xsl:attribute name="onclick">
                                (
                                    function(btn){
                                        var z=document.createElement('script');
                                        document.subtomeBtn=btn;
                                        z.src='https://www.subtome.com/load.js';document.body.appendChild(z);
                                    }
                                )(this);
                                return false;
                            </xsl:attribute>
                        </a>
                        <a title='Learn about syndication feed' onclick='document.getElementById("aboutfeed").classList.toggle("show");' class='popup'><span class='popuptext' id='aboutfeed'></span></a>
                        <a href='https://wikiless.org/wiki/Template:Aggregators' title='Get a feed reader for desktop and mobile'></a>
                        <a href='https://wikiless.org/wiki/RSS' title='Learn the benefits of using feeds for personal and corporates'></a>
                    </section>
                    <xsl:choose>
                    <xsl:when test='atom:entry'>
                    <div id='toc'>
                        <!-- xsl:for-each select='atom:entry[position() &lt;21]' -->
                        <xsl:for-each select='atom:entry[not(position() >20)]'>
                                <xsl:if test='atom:title'>
                                <xsl:element name='a'>
                                    <xsl:attribute name='href'>
                                         <xsl:text>#newspaper-oujs-</xsl:text>
                                         <xsl:value-of select='position()'/>
                                     </xsl:attribute>
                                      <xsl:value-of select='atom:title'/>
                                </xsl:element>
                                </xsl:if>
                        </xsl:for-each>
                    </div>
                    </xsl:when>
                    </xsl:choose>
                    <section id='articles'>
                    <!-- feed entry -->
                    <xsl:choose>
                    <xsl:when test='atom:entry'>
                    <xsl:for-each select='atom:entry[not(position() >20)]'>
                        <div class='entry'>
                            <!-- entry title -->
                            <xsl:if test='atom:title'>
                                <div class='title'>
                                    <xsl:element name='a'>
                                        <xsl:attribute name='href'>
                                            <xsl:choose>
                                                <xsl:when test='atom:link[contains(@rel,"alternate")]'>
                                                    <xsl:value-of select='atom:link[contains(@rel,"alternate")]/@href'/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of select='atom:link/@href'/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:attribute>
                                        <xsl:attribute name='title'>
                                            <xsl:value-of select='atom:title'/>
                                        </xsl:attribute>
                                        <xsl:attribute name='id'>
                                            <xsl:text>newspaper-oujs-</xsl:text>
                                            <xsl:value-of select='position()'/>
                                        </xsl:attribute>
                                        <xsl:value-of select='atom:title'/>
                                    </xsl:element>
                                </div>
                            </xsl:if>
                            <!-- geographic location -->
                            <xsl:choose>
                                <xsl:when test='geo:lat and geo:long'>
                                    <xsl:variable name='lat' select='geo:lat'/>
                                    <xsl:variable name='lng' select='geo:long'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}'>📍</a>
                                    </span>
                                </xsl:when>
                                <xsl:when test='geo:Point'>
                                    <xsl:variable name='lat' select='geo:Point/geo:lat'/>
                                    <xsl:variable name='lng' select='geo:Point/geo:long'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}'>📍</a>
                                    </span>
                                </xsl:when>
                                <xsl:when test='georss:point'>
                                    <xsl:variable name='lat' select='substring-before(georss:point, " ")'/>
                                    <xsl:variable name='lng' select='substring-after(georss:point, " ")'/>
                                    <xsl:variable name='name' select='georss:featurename'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}' title='{$name}'>📍</a>
                                    </span>
                                </xsl:when>
                            </xsl:choose>
                            <!-- div class='posted' -->
                            <!-- entry author -->
                            <!-- xsl:if test='atom:author'>
                                <span class='author'>
                                    <xsl:choose>
                                        <xsl:when test='atom:author/atom:email'>
                                            <xsl:element name='a'>
                                                <xsl:attribute name='href'>
                                                    <xsl:text>mailto:</xsl:text>
                                                    <xsl:value-of select='atom:author/atom:email'/>
                                                </xsl:attribute>
                                                <xsl:attribute name='title'>
                                                    <xsl:text>Send an Email to </xsl:text>
                                                    <xsl:value-of select='atom:author/atom:email'/>
                                                </xsl:attribute>
                                                <xsl:value-of select='atom:author/atom:name'/>
                                            </xsl:element>
                                        </xsl:when>
                                        <xsl:when test='atom:author/atom:uri'>
                                            <xsl:element name='a'>
                                                <xsl:attribute name='href'>
                                                    <xsl:value-of select='atom:author/atom:uri'/>
                                                </xsl:attribute>
                                                <xsl:attribute name='title'>
                                                    <xsl:value-of select='atom:author/atom:summary'/>
                                                </xsl:attribute>
                                                <xsl:value-of select='atom:author/atom:name'/>
                                            </xsl:element>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select='atom:name'/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </span>
                            </xsl:if -->
                            <!-- entry date -->
                            <xsl:choose>
                                <xsl:when test='atom:updated'>
                                    <div class='updated'>
                                        <xsl:value-of select='atom:updated'/>
                                    </div>
                                </xsl:when>
                                <xsl:when test='atom:published'>
                                    <div class='published'>
                                        <xsl:value-of select='atom:published'/>
                                    </div>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='warning atom1 published'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                            <!-- /div -->
                            <!-- entry content -->
                            <!-- entry summary of GitLab Atom Syndication Feeds -->
                            <xsl:if test='atom:content or atom:summary'>
                                <div class='content'>
                                    <xsl:choose>
                                        <xsl:when test='atom:summary[contains(@type,"text")]'>
                                            <xsl:attribute name='type'>
                                                <xsl:value-of select='atom:summary/@type'/>
                                            </xsl:attribute>
                                            <xsl:value-of select='atom:summary'/>
                                        </xsl:when>
                                        <xsl:when test='atom:summary[contains(@type,"base64")]'>
                                            <!-- TODO add xsl:template to handle inline media -->
                                        </xsl:when>
                                        <xsl:when test='atom:content[contains(@type,"text")]'>
                                            <xsl:attribute name='type'>
                                                <xsl:value-of select='atom:content/@type'/>
                                            </xsl:attribute>
                                            <xsl:value-of select='atom:content'/>
                                        </xsl:when>
                                        <xsl:when test='atom:content[contains(@type,"base64")]'>
                                            <!-- TODO add xsl:template to handle inline media -->
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:choose>
                                                <xsl:when test='atom:summary and not(atom:summary="")'>
                                                    <xsl:value-of select='atom:summary' disable-output-escaping='yes'/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:value-of select='atom:content' disable-output-escaping='yes'/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </div>
                            </xsl:if>
                            <!-- entry enclosure -->
                            <xsl:if test='atom:link[contains(@rel,"enclosure")]'>
                                <div class='enclosure' title='Right-click and Save link as…'>
                                    <xsl:for-each select='atom:link[contains(@rel,"enclosure")]'>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='icon'>
                                                <xsl:value-of select='substring-before(@type,"/")'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='@href'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='download'/>
                                            <xsl:call-template name='extract-filename'>
                                                <xsl:with-param name='url' select='@href' />
                                            </xsl:call-template>
                                        </xsl:element>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='class'>
                                                <xsl:value-of select='substring-before(@type,"/")'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:if test='@length &gt; 0'>
                                            <xsl:call-template name='transform-filesize'>
                                                <xsl:with-param name='length' select='@length' />
                                            </xsl:call-template>
                                        </xsl:if>
                                        <xsl:element name='br'/>
                                    </xsl:for-each>
                                    <xsl:for-each select='media:content'>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='icon'>
                                                <xsl:value-of select='@medium'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='@url'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='download'/>
                                            <xsl:call-template name='extract-filename'>
                                                <xsl:with-param name='url' select='@url' />
                                            </xsl:call-template>
                                        </xsl:element>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='class'>
                                                <xsl:value-of select='@medium'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:if test='@fileSize &gt; 0'>
                                            <xsl:call-template name='transform-filesize'>
                                                <xsl:with-param name='length' select='@fileSize' />
                                            </xsl:call-template>
                                        </xsl:if>
                                        <xsl:element name='br'/>
                                    </xsl:for-each>
                                </div>
                            </xsl:if>
                        </div>
                        <!-- entry id -->
                        <xsl:if test='not(atom:id)'>
                            <div class='warning atom1 id'></div>
                        </xsl:if>
                    </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>
                        <div class='notice no-entry'></div>
                    </xsl:otherwise>
                    </xsl:choose>
                    </section>
                </div>
            </body>
        </html>
    </xsl:template>

    
    <!-- RDF 1.1 Syndication Format -->
    <xsl:output
    media-type='application/rdf+xml' />
    <xsl:template match='/rdf:RDF'>
        <!-- index right-to-left language codes -->
        <xsl:variable name='rtl'
        select='channel/language[
        contains(text(),"ar") or 
        contains(text(),"fa") or 
        contains(text(),"he") or 
        contains(text(),"ji") or 
        contains(text(),"ku") or 
        contains(text(),"ur") or 
        contains(text(),"yi")]'/>
        <html>
            <head>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"description"' />
                    <xsl:with-param name='content' select='foo:channel/foo:description' />
                </xsl:call-template>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"generator"' />
                    <xsl:with-param name='content' select='"syndication4humans https://sjehuda.github.io/"' />
                </xsl:call-template>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"mimetype"' />
                    <xsl:with-param name='content' select='"application/rdf+xml"' />
                </xsl:call-template>
                <title>
                    <xsl:choose>
                        <xsl:when test='foo:channel/foo:title and not(foo:channel/foo:title="")'>
                            <xsl:value-of select='foo:channel/foo:title'/>
                        </xsl:when>
                        <xsl:otherwise>Streamburner News Reader</xsl:otherwise>
                    </xsl:choose>
                </title>
                <!-- TODO media='print' -->
                <link href='streamburner.css' rel='stylesheet' type='text/css' media='screen'/>
                <!-- whether language code is of direction right-to-left -->
                <xsl:if test='$rtl'>
                    <link id='semitic' href='syndication-rtl.css' rel='stylesheet' type='text/css' />
                </xsl:if>
            </head>
            <body>
                <div id='feed'>
                    <header>
                        <!-- feed title -->
                        <div id='title'>
                            <xsl:choose>
                                <xsl:when test='foo:channel/foo:title and not(foo:channel/foo:title="")'>
                                    <xsl:attribute name='title'>
                                        <xsl:value-of select='foo:channel/foo:title'/>
                                    </xsl:attribute>
                                    <xsl:value-of select='foo:channel/foo:title'/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='empty'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                        </div>
                        <!-- feed subtitle -->
                        <xsl:choose>
                            <xsl:when test='foo:channel/itunes:subtitle'>
                                <div id='subtitle'>
                                    <xsl:attribute name='title'>
                                        <xsl:value-of select='foo:channel/itunes:subtitle'/>
                                    </xsl:attribute>
                                    <xsl:value-of select='foo:channel/itunes:subtitle'/>
                                </div>
                            </xsl:when>
                            <xsl:when test='foo:channel/foo:description'>
                                <div id='subtitle'>
                                    <xsl:attribute name='title'>
                                        <xsl:value-of select='foo:channel/foo:description'/>
                                    </xsl:attribute>
                                    <xsl:value-of select='foo:channel/foo:description'/>
                                </div>
                            </xsl:when>
                        </xsl:choose>
                    </header>
                    <section id='links'>
                        <a title='Click to get the latest updates and news' onclick='location.href = "feed:" + location.href'>
                        </a>
                        <a title='Subscribe via SubToMe'>
                            <xsl:attribute name="href">
                                https://www.subtome.com/#/subscribe?feeds=<xsl:value-of select="atom:link[@rel='self']/@href" />
                            </xsl:attribute>
                            <xsl:attribute name="onclick">
                                (
                                    function(btn){
                                        var z=document.createElement('script');
                                        document.subtomeBtn=btn;
                                        z.src='https://www.subtome.com/load.js';document.body.appendChild(z);
                                    }
                                )(this);
                                return false;
                            </xsl:attribute>
                        </a>
                        <a title='Learn about syndication feed' onclick='document.getElementById("aboutfeed").classList.toggle("show");' class='popup'><span class='popuptext' id='aboutfeed'></span></a>
                        <a href='https://wikiless.org/wiki/Template:Aggregators' title='Get a feed reader for desktop and mobile'></a>
                        <a href='https://wikiless.org/wiki/RSS' title='Learn the benefits of using feeds for personal and corporates'></a>
                    </section>
                    <xsl:choose>
                    <xsl:when test='foo:item'>
                    <div id='toc'>
                        <xsl:for-each select='foo:item[not(position() >20)]'>
                                <xsl:choose>
                                    <xsl:when test='itunes:subtitle'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:text>#newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                           </xsl:attribute>
                                           <xsl:value-of select='itunes:subtitle'/>
                                        </xsl:element>
                                    </xsl:when>
                                    <xsl:when test='foo:title'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:text>#newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                           </xsl:attribute>
                                           <xsl:value-of select='foo:title'/>
                                        </xsl:element>
                                    </xsl:when>
                                </xsl:choose>
                        </xsl:for-each>
                    </div>
                    </xsl:when>
                    </xsl:choose>
                    <section id='articles'>
                    <!-- feed entry -->
                    <xsl:choose>
                    <xsl:when test='foo:item'>
                    <xsl:for-each select='foo:item[not(position() >20)]'>
                        <div class='entry'>
                            <!-- entry title -->
                            <xsl:choose>
                                <xsl:when test='itunes:subtitle'>
                                    <div class='title'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='foo:link'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='title'>
                                                <xsl:value-of select='itunes:subtitle'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='id'>
                                                <xsl:text>newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                            </xsl:attribute>
                                            <xsl:value-of select='itunes:subtitle'/>
                                        </xsl:element>
                                    </div>
                                </xsl:when>
                                <xsl:when test='foo:title'>
                                    <div class='title'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='foo:link'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='title'>
                                                <xsl:value-of select='foo:title'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='id'>
                                                <xsl:text>newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                            </xsl:attribute>
                                            <xsl:value-of select='foo:title'/>
                                        </xsl:element>
                                    </div>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='warning rss title'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                            <!-- geographic location -->
                            <xsl:choose>
                                <xsl:when test='geo:lat and geo:long'>
                                    <xsl:variable name='lat' select='geo:lat'/>
                                    <xsl:variable name='lng' select='geo:long'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}'>📍</a>
                                    </span>
                                </xsl:when>
                                <xsl:when test='geo:Point'>
                                    <xsl:variable name='lat' select='geo:Point/geo:lat'/>
                                    <xsl:variable name='lng' select='geo:Point/geo:long'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}'>📍</a>
                                    </span>
                                </xsl:when>
                                <xsl:when test='georss:point'>
                                    <xsl:variable name='lat' select='substring-before(georss:point, " ")'/>
                                    <xsl:variable name='lng' select='substring-after(georss:point, " ")'/>
                                    <xsl:variable name='name' select='georss:featurename'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}' title='{$name}'>📍</a>
                                    </span>
                                </xsl:when>
                            </xsl:choose>
                            <!-- div class='posted' -->
                            <!-- entry author -->
                            <!-- span class='author'>
                            <xsl:choose>
                                <xsl:when test='itunes:author'>
                                    <xsl:value-of select='itunes:author'/>
                                </xsl:when>
                                <xsl:when test='author'>
                                    <xsl:value-of select='author'/>
                                </xsl:when>
                            </xsl:choose>
                            </span-->
                            <!-- entry date -->
                            <xsl:if test='dc:date'>
                                <div class='published'>
                                    <xsl:value-of select='dc:date'/>
                                </div>
                            </xsl:if>
                            <!-- /div -->
                            <!-- entry content -->
                            <xsl:choose>
                                <!-- complete text post -->
                                <xsl:when test='content:encoded'>
                                    <div class='content'>
                                        <xsl:value-of select='content:encoded' disable-output-escaping='yes'/>
                                    </div>
                                </xsl:when>
                                <!-- description of post -->
                                <xsl:when test='foo:description'>
                                    <div class='content'>
                                        <xsl:value-of select='foo:description' disable-output-escaping='yes'/>
                                    </div>
                                </xsl:when>
                                <!-- itunes text post -->
                                <xsl:when test='itunes:summary'>
                                    <div class='content'>
                                        <xsl:value-of select='itunes:summary' disable-output-escaping='yes'/>
                                    </div>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='warning rss description'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                            <!-- entry enclosure -->
                            <xsl:if test='enc:enclosure'>
                                <div class='enclosure' title='Right-click and Save link as…'>
                                    <xsl:for-each select='enc:enclosure'>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='icon'>
                                                <xsl:value-of select='substring-before(@enc:type,"/")'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='@rdf:resource'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='download'/>
                                            <xsl:call-template name='extract-filename'>
                                                <xsl:with-param name='url' select='@rdf:resource' />
                                            </xsl:call-template>
                                        </xsl:element>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='class'>
                                                <xsl:value-of select='substring-before(@enc:type,"/")'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:if test='@enc:length &gt; 0'>
                                            <xsl:call-template name='transform-filesize'>
                                                <xsl:with-param name='length' select='@enc:length' />
                                            </xsl:call-template>
                                        </xsl:if>
                                        <xsl:element name='br'/>
                                    </xsl:for-each>
                                </div>
                            </xsl:if>
                        </div>
                    </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>
                        <div class='notice no-entry'></div>
                    </xsl:otherwise>
                    </xsl:choose>
                    </section>
                </div>
            </body>
        </html>
    </xsl:template>

    
    <!-- RSS 2.0 Syndication Format -->
    <xsl:output
    media-type='application/rss+xml' />
    <xsl:template match='/rss'>
        <!-- index right-to-left language codes -->
        <xsl:variable name='rtl'
        select='channel/language[
        contains(text(),"ar") or 
        contains(text(),"fa") or 
        contains(text(),"he") or 
        contains(text(),"ji") or 
        contains(text(),"ku") or 
        contains(text(),"ur") or 
        contains(text(),"yi")]'/>
        <html>
            <head>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"description"' />
                    <xsl:with-param name='content' select='channel/description' />
                </xsl:call-template>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"generator"' />
                    <xsl:with-param name='content' select='"syndication4humans https://sjehuda.github.io/"' />
                </xsl:call-template>
                <xsl:call-template name='metadata'>
                    <xsl:with-param name='name' select='"mimetype"' />
                    <xsl:with-param name='content' select='"application/rss+xml"' />
                </xsl:call-template>
                <title>
                    <xsl:choose>
                        <xsl:when test='channel/title and not(channel/title="")'>
                            <xsl:value-of select='channel/title'/>
                        </xsl:when>
                        <xsl:otherwise>Streamburner News Reader</xsl:otherwise>
                    </xsl:choose>
                </title>
                <!-- TODO media='print' -->
                <link href='streamburner.css' rel='stylesheet' type='text/css' media='screen'/>
                <!-- whether language code is of direction right-to-left -->
                <xsl:if test='$rtl'>
                    <link id='semitic' href='syndication-rtl.css' rel='stylesheet' type='text/css' />
                </xsl:if>
            </head>
            <body>
                <div id='feed'>
                    <header>
                        <!-- feed title -->
                        <div id='title'>
                            <xsl:choose>
                                <xsl:when test='channel/title and not(channel/title="")'>
                                    <xsl:attribute name='title'>
                                        <xsl:value-of select='channel/title'/>
                                    </xsl:attribute>
                                    <xsl:value-of select='channel/title'/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='empty'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                        </div>
                        <!-- feed subtitle -->
                        <xsl:choose>
                            <xsl:when test='channel/itunes:subtitle'>
                                <div id='subtitle'>
                                    <xsl:attribute name='title'>
                                        <xsl:value-of select='channel/itunes:subtitle'/>
                                    </xsl:attribute>
                                    <xsl:value-of select='channel/itunes:subtitle'/>
                                </div>
                            </xsl:when>
                            <xsl:when test='channel/description'>
                                <div id='subtitle'>
                                    <xsl:attribute name='title'>
                                        <xsl:value-of select='channel/description'/>
                                    </xsl:attribute>
                                    <xsl:value-of select='channel/description'/>
                                </div>
                            </xsl:when>
                        </xsl:choose>
                    </header>
                    <section id='links'>
                        <a title='Click to get the latest updates and news' onclick='location.href = "feed:" + location.href'>
                        </a>
                        <a title='Subscribe via SubToMe'>
                            <xsl:attribute name="href">
                                https://www.subtome.com/#/subscribe?feeds=<xsl:value-of select="atom:link[@rel='self']/@href" />
                            </xsl:attribute>
                            <xsl:attribute name="onclick">
                                (
                                    function(btn){
                                        var z=document.createElement('script');
                                        document.subtomeBtn=btn;
                                        z.src='https://www.subtome.com/load.js';document.body.appendChild(z);
                                    }
                                )(this);
                                return false;
                            </xsl:attribute>
                        </a>
                        <a title='Learn about syndication feed' onclick='document.getElementById("aboutfeed").classList.toggle("show");' class='popup'><span class='popuptext' id='aboutfeed'></span></a>
                        <a href='https://wikiless.org/wiki/Template:Aggregators' title='Get a feed reader for desktop and mobile'></a>
                        <a href='https://wikiless.org/wiki/RSS' title='Learn the benefits of using feeds for personal and corporates'></a>
                    </section>
                    <xsl:choose>
                    <xsl:when test='channel/item'>
                    <div id='toc'>
                        <xsl:for-each select='channel/item[not(position() >20)]'>
                                <xsl:choose>
                                    <xsl:when test='itunes:subtitle'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:text>#newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                           </xsl:attribute>
                                           <xsl:value-of select='itunes:subtitle'/>
                                        </xsl:element>
                                    </xsl:when>
                                    <xsl:when test='title'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:text>#newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                           </xsl:attribute>
                                           <xsl:value-of select='title'/>
                                        </xsl:element>
                                    </xsl:when>
                                </xsl:choose>
                        </xsl:for-each>
                    </div>
                    </xsl:when>
                    </xsl:choose>
                    <section id='articles'>
                    <!-- feed entry -->
                    <xsl:choose>
                    <xsl:when test='channel/item'>
                    <xsl:for-each select='channel/item[not(position() >20)]'>
                        <div class='entry'>
                            <!-- entry title -->
                            <xsl:choose>
                                <xsl:when test='itunes:subtitle'>
                                    <div class='title'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='link'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='title'>
                                                <xsl:value-of select='itunes:subtitle'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='id'>
                                                <xsl:text>newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                            </xsl:attribute>
                                            <xsl:value-of select='itunes:subtitle'/>
                                        </xsl:element>
                                    </div>
                                </xsl:when>
                                <xsl:when test='title'>
                                    <div class='title'>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='link'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='title'>
                                                <xsl:value-of select='title'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='id'>
                                                <xsl:text>newspaper-oujs-</xsl:text>
                                                <xsl:value-of select='position()'/>
                                            </xsl:attribute>
                                            <xsl:value-of select='title'/>
                                        </xsl:element>
                                    </div>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='warning rss2 title'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                            <!-- geographic location -->
                            <xsl:choose>
                                <xsl:when test='geo:lat and geo:long'>
                                    <xsl:variable name='lat' select='geo:lat'/>
                                    <xsl:variable name='lng' select='geo:long'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}'>📍</a>
                                    </span>
                                </xsl:when>
                                <xsl:when test='geo:Point'>
                                    <xsl:variable name='lat' select='geo:Point/geo:lat'/>
                                    <xsl:variable name='lng' select='geo:Point/geo:long'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}'>📍</a>
                                    </span>
                                </xsl:when>
                                <xsl:when test='georss:point'>
                                    <xsl:variable name='lat' select='substring-before(georss:point, " ")'/>
                                    <xsl:variable name='lng' select='substring-after(georss:point, " ")'/>
                                    <xsl:variable name='name' select='georss:featurename'/>
                                    <span class='geolocation'>
                                        <a href='geo:{$lat},{$lng}' title='{$name}'>📍</a>
                                    </span>
                                </xsl:when>
                            </xsl:choose>
                            <!-- div class='posted' -->
                            <!-- entry author -->
                            <!-- span class='author'>
                            <xsl:choose>
                                <xsl:when test='itunes:author'>
                                    <xsl:value-of select='itunes:author'/>
                                </xsl:when>
                                <xsl:when test='author'>
                                    <xsl:value-of select='author'/>
                                </xsl:when>
                            </xsl:choose>
                            </span-->
                            <!-- entry date -->
                            <xsl:if test='pubDate'>
                                <div class='published'>
                                    <xsl:value-of select='pubDate'/>
                                </div>
                            </xsl:if>
                            <!-- /div -->
                            <!-- entry content -->
                            <xsl:choose>
                                <!-- complete text post -->
                                <xsl:when test='content:encoded'>
                                    <div class='content'>
                                        <xsl:value-of select='content:encoded' disable-output-escaping='yes'/>
                                    </div>
                                </xsl:when>
                                <!-- description of post -->
                                <xsl:when test='description'>
                                    <div class='content'>
                                        <xsl:value-of select='description' disable-output-escaping='yes'/>
                                    </div>
                                </xsl:when>
                                <!-- itunes text post -->
                                <xsl:when test='itunes:summary'>
                                    <div class='content'>
                                        <xsl:value-of select='itunes:summary' disable-output-escaping='yes'/>
                                    </div>
                                </xsl:when>
                                <xsl:otherwise>
                                    <div class='warning rss2 description'></div>
                                </xsl:otherwise>
                            </xsl:choose>
                            <!-- entry enclosure -->
                            <xsl:if test='enclosure or media:content'>
                                <div class='enclosure' title='Right-click and Save link as…'>
                                    <xsl:for-each select='enclosure'>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='icon'>
                                                <xsl:value-of select='substring-before(@type,"/")'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='@url'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='download'/>
                                            <xsl:call-template name='extract-filename'>
                                                <xsl:with-param name='url' select='@url' />
                                            </xsl:call-template>
                                        </xsl:element>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='class'>
                                                <xsl:value-of select='substring-before(@type,"/")'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:if test='@length &gt; 0'>
                                            <xsl:call-template name='transform-filesize'>
                                                <xsl:with-param name='length' select='@length' />
                                            </xsl:call-template>
                                        </xsl:if>
                                        <xsl:element name='br'/>
                                    </xsl:for-each>
                                    <xsl:for-each select='media:content'>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='icon'>
                                                <xsl:value-of select='@medium'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:element name='a'>
                                            <xsl:attribute name='href'>
                                                <xsl:value-of select='@url'/>
                                            </xsl:attribute>
                                            <xsl:attribute name='download'/>
                                            <xsl:call-template name='extract-filename'>
                                                <xsl:with-param name='url' select='@url' />
                                            </xsl:call-template>
                                        </xsl:element>
                                        <xsl:element name='span'>
                                            <xsl:attribute name='class'>
                                                <xsl:value-of select='@medium'/>
                                            </xsl:attribute>
                                        </xsl:element>
                                        <xsl:if test='@fileSize &gt; 0'>
                                            <xsl:call-template name='transform-filesize'>
                                                <xsl:with-param name='length' select='@fileSize' />
                                            </xsl:call-template>
                                        </xsl:if>
                                        <xsl:element name='br'/>
                                    </xsl:for-each>
                                </div>
                            </xsl:if>
                        </div>
                    </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>
                        <div class='notice no-entry'></div>
                    </xsl:otherwise>
                    </xsl:choose>
                    </section>
                </div>
            </body>
        </html>
    </xsl:template>

    
    <!-- set page metadata -->
    <xsl:template name='metadata'>
        <xsl:param name='name'/>
        <xsl:param name='content'/>
        <xsl:if test='$content and not($content="")'>
            <xsl:element name='meta'>
                <xsl:attribute name='name'>
                    <xsl:value-of select='$name'/>
                </xsl:attribute>
                <xsl:attribute name='content'>
                    <xsl:value-of select='$content'/>
                </xsl:attribute>
            </xsl:element>
        </xsl:if>
    </xsl:template>


    <!-- extract filename from given url string -->
    <!-- 
    <xsl:template name='extract-filename'>
        <xsl:param name='url'/>
        <xsl:choose>
            <xsl:when test='contains($url,"/")'>
                <xsl:call-template name='extract-filename'>
                    <xsl:with-param name='url' select='substring-after($url,"/")'/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select='$url'/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    -->


    <!-- FIXME: RSS 2.0 enclosures of GNU Social / StatusNet -->
    <!-- extract filename from given url string -->
    <xsl:template name='extract-filename'>
        <xsl:param name='url'/>
        <xsl:choose>
            <!-- We check whether there is a dot after domain name so we will not end up with an empty string -->
            <!-- ends-with of XPath 2.0 doesn't work everywhere -->
            <xsl:when test='substring($url, string-length($url) - string-length("/") + 1)  = "/"'>
                <xsl:value-of select='substring-after(substring($url, 1, string-length($url) - 1),"://")'/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test='contains($url,"/") and contains(substring-after($url,"/"),".")'>
                        <xsl:call-template name='extract-filename'>
                            <xsl:with-param name='url' select='substring-after($url,"/")'/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select='$url'/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    
    <!-- transform filesize from given length string -->
    <xsl:template name='transform-filesize'>
        <xsl:param name='length'/>
        <!-- TODO consider xsl:decimal-format and xsl:number -->
        <xsl:choose>
            <!-- TODO consider removal of Byte -->
            <xsl:when test='$length &lt; 2'>
                <xsl:value-of select='$length'/>
                Byte
            </xsl:when>
            <xsl:when test='floor($length div 1024) &lt; 1'>
                <xsl:value-of select='$length'/>
                Bytes
            </xsl:when>
            <xsl:when test='floor($length div (1024 * 1024)) &lt; 1'>
                <xsl:value-of select='floor($length div 1024)'/>.<xsl:value-of select='substring($length mod 1024,0,2)'/>
                KiB
            </xsl:when>
            <xsl:when test='floor($length div (1024 * 1024 * 1024)) &lt; 1'>
                <xsl:value-of select='floor($length div (1024 * 1024))'/>.<xsl:value-of select='substring($length mod (1024 * 1024),0,2)'/>
                MiB
            </xsl:when>
            <xsl:otherwise>
                <!-- P2P links -->
                <xsl:value-of select='floor($length div (1024 * 1024 * 1024))'/>.<xsl:value-of select='substring($length mod (1024 * 1024 * 1024),0,2)'/>
                GiB
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    
</xsl:stylesheet>
