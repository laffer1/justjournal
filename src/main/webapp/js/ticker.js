function addEventToObject(obj, evt, func) {
    var oldhandler = obj[evt];
    obj[evt] = (typeof obj[evt] != 'function') ? func : function(ev) {
        oldhandler(ev);
        func(ev);
    };
}
function ajaxRequest(url, func, obj) {
    if (window.XMLHttpRequest) {
        var req = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        try {
            req = new ActiveXObject("Msxml2.XMLHTTP");
        } catch(e) {
            req = new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    if (func) {
        req.onreadystatechange = function() {
            func(req, obj);
        }
    }
    req.open('GET', url, true);
    req.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    req.setRequestHeader('If-Modified-Since', 'Wed, 15 Nov 1995 00:00:00 GMT');
    if (req.overrideMimeType) {
        req.overrideMimeType("text/xml");
    }
    req.send(null);
    return false;
}

var rssFetchAndDisplay = {
    aFeeds: [[['recent-blogs'],['The latest blog entries on jj.'],['<a href="/">Recent Blog Entries <\/a>'],['http://www.justjournal.com/RecentBlogs']]],
    iCurrentFeedsItem: 0,
    aNews: new Array(),
    iCurrentNewsItem: 0,
    tTM: null,
    sState: 'hide',
    iCycleCount: 2000,
    iFadeInCount: 110,
    iFadeOutCount: 110,
    Init: function()
    {
        var r = rssFetchAndDisplay;
        r.SetXml('/RecentBlogs');
    },
    SetXml: function(sUrl)
    {
        var r = rssFetchAndDisplay;
        ajaxRequest(sUrl, r.XmlFetchResponse);
    },
    XmlFetchResponse: function(req)
    {
        var r = rssFetchAndDisplay;
        if (req.readyState == 4)
        {
            if (req.status == 200)
            {
                if (r.tTM) {
                    clearTimeout(r.tTM);
                }
                r.Create(req);
            }
            else
            {
                alert("There was a problem retrieving the XML data:\n\r" + req.statusText);
            }
        }
    },
    Create: function(req)
    {
        var r = rssFetchAndDisplay;
        r.iCurrentNewsItem = 0,
                r.ParseXMLToArray(req);
        var oTickerCont = document.getElementById('ticker');
        oTickerCont.innerHTML = "\n\r" + '<table border="0" cellpadding="0" cellspacing="0" class="' + r.sState + '">' + "\n\r" + '<tr><td id="tic-title"><span>' + r.aFeeds[r.iCurrentFeedsItem][2] + '</span></td><td id="tic-item"><a href="' + r.aNews[r.iCurrentNewsItem].url + '">' + r.aNews[r.iCurrentNewsItem].title + '</a></td></tr>' + "\n\r" + '</table>' + "\n\r";

        var aHrefs = document.getElementById('tic-item').getElementsByTagName('a');
        addEventToObject(aHrefs[0], 'onmouseover', r.PauseTicker);
        addEventToObject(aHrefs[0], 'onmouseout', r.RestartTicker);
        r.CycleTicker();
    },
    ParseXMLToArray: function(req)
    {
        var r = rssFetchAndDisplay;
        if (req.responseXML.documentElement)
        {
            aItems = req.responseXML.documentElement.getElementsByTagName('item');
        }
        else
        {
            dom = new ActiveXObject("MSXML.DOMDocument");
            dom.loadXML(req.responseText);
            aItems = dom.documentElement.getElementsByTagName('item');
        }
        for (var i = 0; i < aItems.length; i++)
        {
            r.aNews[i] = new Array();
            r.aNews[i].title = aItems[i].getElementsByTagName('title')[0].firstChild.nodeValue;
            r.aNews[i].url = aItems[i].getElementsByTagName('link')[0].firstChild.nodeValue;
        }
    },
    PauseTicker: function()
    {
        var r = rssFetchAndDisplay;
        if (r.tTM) {
            clearTimeout(r.tTM)
        }
        ;
        var oFadeItem = document.getElementById('tic-item').getElementsByTagName('a')[0];
        oFadeItem.style.KHTMLOpacity = 0.999;
        // Safari<1.2, Konqueror
        oFadeItem.style.MozOpacity = 0.999;
        // Older Mozilla and Firefox
        oFadeItem.style.opacity = 0.999;
        // Safari 1.2, newer Firefox and Mozilla, CSS3
        //obj.style.zoom = 1; // bizarre ie fix
        //obj.style.filter = 'alpha(opacity:'+obj.fadeCount+')'; // IE/Win
        r.iFadeOutCount = 100;
    },
    RestartTicker: function()
    {
        var r = rssFetchAndDisplay;
        r.iCycleCount = 2000;
        r.CycleTicker();
    },
    CycleTicker: function()
    {
        var r = rssFetchAndDisplay;
        if (r.tTM) {
            clearTimeout(r.tTM);
        }
        if (r.iCycleCount < 0)
        {
            r.iCycleCount = 2000;
            r.FadeOut();
        }
        else
        {
            r.iCycleCount -= 100;
            //var iDelay = (r.iCurrentNewsItem == 0) ? 0 : 100;
            r.tTM = setTimeout(function() {
                r.CycleTicker()
            }, 100);
        }
    },
    FadeOut: function()
    {
        var r = rssFetchAndDisplay;
        if (r.tTM) {
            clearTimeout(r.tTM)
        }
        ;
        if (r.iFadeOutCount < 0)
        {
            r.iCurrentNewsItem = (r.iCurrentNewsItem < r.aNews.length - 1) ? r.iCurrentNewsItem + 1 : 0;
            var oActiveItem = document.getElementById('tic-item');
            oActiveItem.innerHTML = '<a href="' + r.aNews[r.iCurrentNewsItem].url + '">' + r.aNews[r.iCurrentNewsItem].title + '</a>';
            var aHrefs = document.getElementById('tic-item').getElementsByTagName('a');
            addEventToObject(aHrefs[0], 'onmouseover', r.PauseTicker);
            addEventToObject(aHrefs[0], 'onmouseout', r.RestartTicker);
            r.iFadeOutCount = 110;
            r.CycleTicker();
        }
        else
        {
            r.iFadeOutCount -= 10;
            if (r.iFadeOutCount <= 100)
            {
                var oFadeItem = document.getElementById('tic-item').getElementsByTagName('a')[0];
                var opac = ((r.iFadeOutCount / 100) > 0.999) ? 0.999 : (r.iFadeOutCount / 100);
                oFadeItem.style.KHTMLOpacity = opac;
                // Safari<1.2, Konqueror
                oFadeItem.style.MozOpacity = opac;
                // Older Mozilla and Firefox
                oFadeItem.style.opacity = opac;
                // Safari 1.2, newer Firefox and Mozilla, CSS3
                //obj.style.zoom = 1; // bizarre ie fix
                //obj.style.filter = 'alpha(opacity:'+obj.fadeCount+')'; // IE/Win
            }
            r.tTM = setTimeout(function() {
                r.FadeOut()
            }, 50);
        }
    }
}

addEventToObject(window, 'onload', rssFetchAndDisplay.Init);
