UPDATE entry set format='HTML' where autoformat = 'N' and format!='MARKDOWN';
UPDATE entry set format='TEXT' where autoformat = 'Y' and format!='MARKDOWN';