# controleRempes
Salut les  Rempes !

Voici un petit programme en Java 1.8 qui tourne sous windows ou linux (qui devrait aussi tourner en java 1.7 – j’ai pas testé) et qui peut être utile pour vos ados si vous utilisez le contrôle parental de la freeBox-V6.
Il leur permet de connaître leurs autorisations d’accès au web avec un tableau similaire à l’interface web de la freebox. 
Un système d’alerte audio paramétrable lui permet d’être averti avant un changement d’état de son autorisation d’accès. 

Pour la première connexion suivez bien les instructions. Une clé est enregistrée sur le répertoire d’installation. Pour cela vous aurez besoin de l’accès à la freebox. Ensuite plus besoin de mot de passe l’application se connecte toute seule. L’application ne fait que lire les données de la freebox et ne permet en aucun cas de les modifier.

Pour les développeurs, c’est du pur java swing codé sous eclipse/Ubuntu avec quelques librairies pour faire les requêtes http à la box. Le package freebox peut être réutilisé facilement pour d’autres projets.
 
Pour l’installation, il suffit de dézipper le fichier « controlRempes.zip » puis exécuter le .bat ou .sh selon votre os.

Foxpaps
