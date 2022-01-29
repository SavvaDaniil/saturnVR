<?php
/*
for($i = 0;$i<1;$i=$i+0.01){
echo '
if(new_change_sky == '.$i.'){<br />
	document.getElementById("sky_1").setAttribute("material","opacity","'.round((1-$i), 2).'");<br />
	document.getElementById("sky_2").setAttribute("material","opacity","'.$i.'");<br />
}<br />';
}
*/


/*
$schetchik = 1;
  $dir = './images/'; // Папка с изображениями
 
  $files = scandir($dir); // Берём всё содержимое директории

  for ($i_of_slide = 0; $i_of_slide < count($files); $i_of_slide++) { // Перебираем все файлы
    if (($files[$i_of_slide] != ".") && ($files[$i_of_slide] != "..")) { // Текущий каталог и родительский пропускаем
      
      //$path = $dir.$files[$i_of_slide]; // Получаем путь к картинке
	  
      echo $schetchik . " - ".$files[$i_of_slide]."<br />";
	rename('./images/'.$files[$i_of_slide],'./images/'.$schetchik.".jpg");
	  
	  $schetchik++;
    }
  }
*/


for($i = 2;$i<=118;$i++){
	
//echo htmlspecialchars('<img src="images/'.$i.'.jpg" id="images_'.$i.'">');
echo htmlspecialchars('<a-entity geometry="primitive:sphere;radius:101;segmentsWidth:32;segmentsHeight:32" material="shader:flat;color:#ffffff;fog:false; src:images/'.$i.'.jpg;opacity:0;" scale="-1 1 1" rotation="0 0 0" id="sky_'.$i.'"></a-entity>');
echo '<br />';
}

?>