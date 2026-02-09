Uppgift 1:
Room & BookingSystem

Denna del av projektet innehåller implementation av tester för `Room`,
`Booking` och `BookingSystem`.

`Room` ansvarar för att hantera bokningar och tillgänglighet samt att
identifiera överlappande tidsintervall.

`BookingSystem` hanterar bokningsflödet, validering av indata och avbokningar.
Tester täcker både normala scenarion och kantfall, t.ex. ogiltiga tider,
icke-existerande rum eller bokningar samt felaktiga avbokningar.

Alla tester passerar.

---------------------------
Uppgift 2:
ShoppingCart made with TDD

I den här uppgiften har jag implementerat en `ShoppingCart` i Java med fokus på Test Driven Development (TDD).

Jag har arbetat stegvis enligt; Red–Green–Refactor:
- först skrivit ett test som beskriver önskat beteende
- därefter implementerat minsta möjliga kod för att få testet grönt
- och sedan refaktorerat vid behov

ShoppingCart hanterar:
- att lägga till och ta bort varor
- uppdatering av kvantiteter
- beräkning av totalpris
- procentuell och/eller fast rabatt

Jag har även lagt till kanttester för t.ex.:
- negativa eller noll-kvantiteter
- negativa rabatter
- rabatt som överstiger totalpriset
- borttagning av varor som inte finns

Alla tester är skrivna med beskrivande namn för att tydligt visa
vilket beteende som testas och varför.
