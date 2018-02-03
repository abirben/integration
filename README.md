
# Installation et import des dépendances 

Pour utiliser l’API, il faut créer un projet maven et ajouter la dépendance suivante :

 <dependency>

      <groupId>eu.ensg.abn</groupId>
      <artifactId>project</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <scope>system</scope>
      <systemPath>le chemin du JAR</systemPath>
 
</dependency>


Il faut aussi ajouter les dépendences  et les propriétés et les dépendances des API utilitaires utilisées (geotools…) :

<properties>
    
     <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
     <geotools.version>19-SNAPSHOT</geotools.version>
 
 </properties>

  <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-shapefile</artifactId>
            <version>${geotools.version}</version>
  </dependency>

  <dependency> 
            <groupId>org.geotools</groupId>
            <artifactId>gt-swing</artifactId>
            <version>${geotools.version}</version>
  </dependency>


<repositories>
        
        <repository>
            <id>maven2-repository.dev.java.net</id>
            <name>Java.net repository</name>
            <url>http://download.java.net/maven/2</url>
        </repository>
        
        <repository>
            <id>osgeo</id>
            <name>Open Source Geospatial Foundation Repository</name>
            <url>http://download.osgeo.org/webdav/geotools/</url>
        </repository>
        
        <repository>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
          <id>boundless</id>
          <name>Boundless Maven Repository</name>
          <url>http://repo.boundlessgeo.com/main</url>
        </repository>
    
</repositories>


# Création des géométries 
 
L'API propose des fonctionalitées pour créer à partir d'un nuage de points:
-Une Enveloppe Convexe
-Un Minimal Bounding Oriented Box
-Un Minmal Bonding Box

# Nuage de points 

-Créer une nouvelle instance de la classe Coordinate de Java Topololy Suite(JTS)
   exemple :

Coordinate[] pts = new Coordinate[] {new Coordinate(46.06,11.11 ), new Coordinate(40.00,93.08),new Coordinate(20.90,13.08)};

-Créer un nuage de points à partir de la séquence de coordonnées pts définis précedement

  NuagePoint nuage = new NuagePoint(pts,pts.length);

-Exporter le nuage de points en shapefile

  nuage.exportShapefile();


# Enveloppe Convexe en utilisant l'algorithme de Graham

- Il faut simplement créer un nuage de points comme décrit ci dessus et ensuite créer l'enveloppe comme suit :

 EnveloppeConvexeGraham envg = new EnveloppeConvexeGraham(nuage);

- Pour exporter l'enveloppe en fichier shapefile :

  envg.exportShapefile();



# Enveloppe Convexe en utilisant la marche de Jarvis

- Il faut simplement créer un nuage de points comme décrit ci dessus et ensuite créer l'enveloppe comme suit :

 EnveloppeConvexeJarvis envj = new EnveloppeConvexeJarvis(nuage);

- Pour exporter l'enveloppe en fichier shapefile :

 envj.exportShapefile() ;



# Minimal Bounding box

-C'est exactement de la mème façon que la création des enveloppes, à partir d'un nuage de points , on créer une nouvelle instance de la classe BoundingBox :

BoundingBox box = new BoundingBox(nuage);
 
- Export de la géométrie en shapefile :

box.exportShapefile() ;


# Oriented Minimal Bounding box

-C'est exactement de la mème façon que la création des enveloppes, à partir d'un nuage de points , on crée une nouvelle instance de la classe BoundingBox :

OrientedBoundingBox obox = new OrientedBoundingBox(nuage);
 
- Export de la géométrie en shapefile :

obox.exportShapefile() ;
   


# Remarque 

Il se peut que lors le test d’une classe pour exporter le shape file, le message suivant s’afficherait : ‘ le shape file n’est pas écrit ‘, il faut juste réxécuter une deuxième fois et  vous trouverez le shape file dans la racine du projet .




