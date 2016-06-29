/***********************************************
 * Switch Content script- Dynamic Drive (www.dynamicdrive.com)
 * This notice must stay intact for legal use. Last updated April 2nd, 2005.
 * Visit http://www.dynamicdrive.com/ for full source code
 ***********************************************/

var enablepersist = "on"; //Enable saving state of content structure using session cookies? (on/off)
var collapseprevious = "no"; //Collapse previously open content when opening present? (yes/no)

var contractsymbol = '- '; //HTML for contract symbol. For image, use: <img src="whatever.gif">
var expandsymbol = '+ '; //HTML for expand symbol.


if (document.getElementById) {
    jQuery('body').append('<style type="text/css">.switchcontent{display:none;}</style>');
}

function getElementbyClass(rootobj, classname) {
    var temparray = [];
    var inc = 0;
    var rootlength = rootobj.length;
    for (var i = 0; i < rootlength; i++) {
        if (rootobj[i].className === classname) {
            temparray[inc++] = rootobj[i];
        }
    }
    return temparray;
}

function sweeptoggle(ec) {
    var thestate = (ec === "expand") ? "block" : "none";
    var inc = 0;
    while (ccollect[inc]) {
        ccollect[inc].style.display = thestate;
        inc++;
    }
    revivestatus();
}


function contractcontent(omit) {
    var inc = 0;
    while (ccollect[inc]) {
        if (ccollect[inc].id != omit)
            ccollect[inc].style.display = "none";
        inc++;
    }
}

function expandcontent(curobj, cid) {
    var spantags = curobj.getElementsByTagName("span");
    var showstateobj = getElementbyClass(spantags, "showstate");
    if (ccollect.length > 0) {
        if (collapseprevious === "yes") {
            contractcontent(cid);
        }
        document.getElementById(cid).style.display = (document.getElementById(cid).style.display != "block") ? "block" : "none";
        if (showstateobj.length > 0) { //if "showstate" span exists in header
            if (collapseprevious == "no") {
                showstateobj[0].innerHTML = (document.getElementById(cid).style.display == "block") ? contractsymbol : expandsymbol;
            }
            else {
                revivestatus();
            }
        }
    }
}

function revivecontent() {
    contractcontent("omitnothing");
    var selectedItem = getselectedItem();
    var selectedComponents = selectedItem.split("|");
    for (var i = 0; i < selectedComponents.length - 1; i++) {
        document.getElementById(selectedComponents[i]).style.display = "block";
    }
}

function revivestatus() {
    var inc = 0;
    while (statecollect[inc]) {
        if (ccollect[inc].style.display == "block") {
            statecollect[inc].innerHTML = contractsymbol;
        }
        else {
            statecollect[inc].innerHTML = expandsymbol;
        }
        inc++;
    }
}

function get_cookie(Name) {
    var search = Name + "=";
    var returnvalue = "";
    var offset;
    if (document.cookie.length > 0) {
        offset = document.cookie.indexOf(search);
        if (offset !== -1) {
            offset += search.length;
            end = document.cookie.indexOf(";", offset);
            if (end == -1) end = document.cookie.length;
            returnvalue = unescape(document.cookie.substring(offset, end));
        }
    }
    return returnvalue;
}

function getselectedItem() {
    if (get_cookie(window.location.pathname) !== "") {
        selectedItem = get_cookie(window.location.pathname);
        return selectedItem;
    }
    else {
        return "";
    }
}

function saveswitchstate() {
    var inc = 0, selectedItem = "";
    while (ccollect[inc]) {
        if (ccollect[inc].style.display === "block") {
            selectedItem += ccollect[inc].id + "|";
        }
        inc++;
    }

    document.cookie = window.location.pathname + "=" + selectedItem;
}

function do_onload() {
    uniqueidn = window.location.pathname + "firsttimeload";
    var alltags = document.all ? document.all : document.getElementsByTagName("*");
    ccollect = getElementbyClass(alltags, "switchcontent");
    statecollect = getElementbyClass(alltags, "showstate");
    if (enablepersist === "on" && ccollect.length > 0) {
        document.cookie = (get_cookie(uniqueidn) === "") ? uniqueidn + "=1" : uniqueidn + "=0";
        firsttimeload = (get_cookie(uniqueidn) === 1) ? 1 : 0; //check if this is 1st page load
        if (!firsttimeload) {
            revivecontent();
        }
    }
    if (ccollect.length > 0 && statecollect.length > 0) {
        revivestatus();
    }
}

if (window.addEventListener) {
    window.addEventListener("load", do_onload, false);
}
else if (window.attachEvent) {
    window.attachEvent("onload", do_onload);
}
else if (document.getElementById) {
    window.onload = do_onload;
}

if (enablepersist === "on" && document.getElementById) {
    window.onunload = saveswitchstate;
}