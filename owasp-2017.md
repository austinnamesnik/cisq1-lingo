# Vulnerability Analysis

## A5:2017 - Broken Access Control

### 1. Wat houdt de vulnerability in?

De vulnerability houdt in dat iemand toegang probeert te krijgen tot data 
die niet voor diegene bestemd is. Of dat iemand functionaliteit probeert
te bemachtigen die niet bestemd is voor diegene.
Dit kan behaald worden door bijvoorbeeld een URL-parameter
aan te passen of om een URL geforceerd te bezoeken.

### 2. Hoe groot is het risico voor deze kwetsbaarheid binnen het project?

De kwetsbaarheid binnen het project heeft geen groot risico.
Er wordt (nog) niet gewerkt met authenticatie binnen het project,
waardoor alle data toegankelijk is voor iedereen die de applicatie
kan bereiken.

#### 2a. Wat als we authenticatie en autorisatie toevoegen?

Het risico is niet hoog. Dit project wordt gedreven door tests (TDD).
De authenticatie wordt ondersteund door veel testen. Omdat er ook veel
gebruik gemaakt wordt van automatisch testen, wordt er altijd gecontroleerd
op kwetsbaarheden.

### 3. Hoe wordt dit risico tegengegaan binnen het project?

Dit risico wordt tegengegaan door veel gebruik te maken van integratie-/unit-testen.
Ook door gebruik te maken van de OWASP-plugin wordt er getest op allerlei kwetsbaarheden.

## A7:2017 - Cross-Site Scripting

### 1. Wat houdt de vulnerability in?

De vulnerability bevat de volgende attacks: session stealing, account takeover,
MFA bypass (MultiFactorAuthenticatie), DOM node replacement en attacks
tegen de browser van de gebruiker (b.v. malware installeren, key logging).
Het komt neer op het gebruik van scripts om van buitenaf de controle van een applicatie te
bemachtigen. Of om kennis te verkrijgen van de interne werking van de applicatie.

### 2. Hoe groot is het risico voor deze kwetsbaarheid binnen het project?

Binnen het project is het risico op deze kwetsbaarheid miniem. Aan deze applicatie zit
geen Front-End vast, waardoor gegevens niet gehijackt kunnen worden.

#### 2a. Wat als we authenticatie en autorisatie toevoegen?

Het toevoegen van authenticatie en autorisatie heeft geen invloed op het risico.
Er is geen Front-End waar tegenaan gepraat kan worden. Als er een Front-End zou zijn,
dan zou het risico al hoger zijn. Nog steeds gaat het niet om hele persoonlijke
informatie binnen deze applicatie.

### 3. Hoe wordt dit risico tegengegaan binnen het project?

Veel frameworks voorkomen risico's op XSS. Maar zoals bij de vorige vraag werd gezegd:
Er is geen Front-End binnen het project en ook geen authenticatie. Inloggegevens kunnen
niet gestolen worden en de DOM van de gebruiker kan ook niet van buitenaf gemanipuleerd worden.

## A9:2017 - Using Components with Known Vulnerabilities

### 1. Wat houdt de vulnerability in?

"Het gebruik van componenten (frameworks, libraries, etc.) met kwetsbaarheden" dat is wat deze
vulnerability inhoudt. Het is veel voorkomend bij component-heavy development. Dit kan mogelijk
leiden tot weinig of geen kennis van het development team over de componenten die zij gebruiken.
Het is belangrijk om altijd de gebruikte componenten up-to-date te houden en te weten welke versies
de componenten ieder gebruiken.

### 2. Hoe groot is het risico voor deze kwetsbaarheid binnen het project?

Binnen het project is de kans op deze kwetsbaarheid klein, maar nooit nul. Er worden wel
componenten gebruikt in het project, maar het project is er niet 100% van afhankelijk.

#### 2a. Wat als we authenticatie en autorisatie toevoegen?

Voor de authenticatie en autorisatie wordt als het goed is geen nieuw of ander component
gebruikt, dus het risico op kwetsbaarheden zal niet toenemen.

### 3. Hoe wordt dit risico tegengegaan binnen het project?

Binnen het project wordt er op meerdere manieren beschermd tegen deze kwetsbaarheid:
- De plugin van OWASP om automatisch de dependencies te controleren
- Dependabot op GitHub die pull-requests aanraadt als er kwetsbaarheden gevonden zijn in
de dependencies