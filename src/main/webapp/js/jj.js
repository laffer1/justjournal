/*
    Just Journal JavaScript Library
    author: Lucas Holt
    date created: June 10, 2007
*/

function confirmDelete()
{
    'use strict';
    return window.confirm("Are you sure you want to delete this?");
}

function showbox(boxId) {
    'use strict';
    var box = document.getElementById(boxId);
    var parentBox = document.getElementById( boxId + "parent" );
    box.style.top = getAbsY(parentBox) + "px";
    box.style.left = getAbsX(parentBox) + "px";
    box.style.visibility='visible';
}

function hidebox(boxId) {
    'use strict';
    var box = document.getElementById(boxId);
    box.style.visibility='hidden';
}

// get the true X offset of anything on NS4, IE4/5 &
// NS6, even if it's in a table!
function getAbsX(elt) {
    'use strict';
    return (elt.x) ? elt.x : getAbsPos(elt,"Left");
}

// get the true Y offset of anything on NS4, IE4/5 &
// NS6, even if it's in a table!
function getAbsY(elt) {
    'use strict';

    return (elt.y) ? elt.y : getAbsPos(elt,"Top");
}

function getAbsPos(elt,which) {
    'use strict';
    var iPos = 0;

    while (elt !== null) {
        iPos += elt["offset" + which];
        elt = elt.offsetParent;
    }
    
    return iPos;
}
