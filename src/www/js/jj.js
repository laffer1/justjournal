/*
    Just Journal JavaScript Library
    author: Lucas Holt
    date created: June 10, 2007
    $Id: jj.js,v 1.1 2007/06/10 18:25:35 laffer1 Exp $
*/

function confirmDelete()
{
    var answer = confirm("Are you sure you want to delete this?");
    return answer;
}

function showbox(boxId) {
    var box = document.getElementById(boxId);
    parentBox = document.getElementById( boxId + "parent" );
    box.style.top = getAbsY(parentBox) + "px";
    box.style.left = getAbsX(parentBox) + "px";
    box.style.visibility='visible';
}

function hidebox(boxId) {
    var box = document.getElementById(boxId);
    box.style.visibility='hidden';
}

// get the true X offset of anything on NS4, IE4/5 &
// NS6, even if it's in a table!
function getAbsX(elt) {
    return (elt.x) ? elt.x : getAbsPos(elt,"Left");
}

// get the true Y offset of anything on NS4, IE4/5 &
// NS6, even if it's in a table!
function getAbsY(elt) {
    return (elt.y) ? elt.y : getAbsPos(elt,"Top");
}

function getAbsPos(elt,which) {
    var iPos = 0;

    while (elt != null) {
        iPos += elt["offset" + which];
        elt = elt.offsetParent;
    }
    
    return iPos;
}
