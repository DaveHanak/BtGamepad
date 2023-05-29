# UMPAnUMiW-Projekt-BtGamepad

BtGamepad to aplikacja na urządzenia z systemem Android w wersji 9 i wzwyż. Pozwala niejako na zamianę telefonu w uniwersalne urządzenie HID typu gamepad, z którym komunikacja odbywa się poprzez Bluetooth. Gamepad, oprócz standardowych przycisków i gałek analogowych, posiada również opcję skorzystania z funkcji żyroskopu i wyboru trybu wprowadzania: trybu spełniającego wymagania API DirectInput typowo pod system Windows lub uniwersalnego dla aplikacji z własnym API do odczytu wejść z gamepada.

Aby rozpocząć zabawę z padem, należy w widoku Menu nacisnąć przycisk START. Jeżeli aplikacji brakuje jakichś uprawnień, po wciśnięciu przycisku użytkownik zostanie najpierw poproszony o ich udzielenie. Postępując zgodnie z wyświetlanymi instrukcjami, użytkownik w końcu poproszony zostanie o połączenie się z urządzeniem, na którym zamierza używać pada. Po pomyślnym połączeniu można nacisnąć przycisk GAMEPAD i przenieść się do widoku umożliwiającego sterowanie. Ostatnie pomyślne połączenie pada zostanie zapamiętane, tak by po ponownym uruchomieniu aplikacji i naciśnięciu przycisku START móc bez problemu wznowić pracę gamepada. Przy włączonym padzie, przycisk STOP wstrzymuje jego pracę. Przejście do widoku Preferences jest możliwe tylko z zatrzymanym gamepadem i odbywa się poprzez naciśnięcie przycisku PREFERENCES w Menu. Widok Preferences umożliwia dostosowanie działania pada pod potrzeby użytkownika lub samej gry, która ma go używać.

Dostępne opcje:
- Trybu wprowadzania: DirectInput lub Raw (uniwersalny),
- Analogowe lub cyfrowe triggery w trybie DirectInput (nacisk na analogowe triggery zmienia się wraz z położeniem miejsca naciśnięcia triggera; im bliżej bocznej krawędzi przycisku LT lub RT najbliższej środka ekranu, tym nacisk jest większy),
- Funkcja żyroskopu (w DirectInput żyroskop zastępuje jedną z gałek analogowych, natomiast w trybie Raw są to dodatkowe 2 osie),
- Wybór gałki analogowej, która ma zostać zastąpiona przez żyroskop w trybie DirectInput.

![btGamepadMenu](https://github.com/DaveHanak/UMPAnUMiW-Projekt-BtGamepad/assets/72354597/98994f4e-ae68-48c5-9723-9c6e93e5394e)
![btGamepadGaming](https://github.com/DaveHanak/UMPAnUMiW-Projekt-BtGamepad/assets/72354597/f45826fa-9fcd-4a3b-9788-f612f238b1d6)
![btGamepadPrefs](https://github.com/DaveHanak/UMPAnUMiW-Projekt-BtGamepad/assets/72354597/c10a11b0-daa3-4a49-8728-a6ca6c2bdf1c)
