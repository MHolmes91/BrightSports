## BrightSports v2.17 — a game you closed stays closed

**Backing out of a game you opened from a notification now leaves you on the feed.** Before this, the game screen kept reopening on the feed for no reason, again and again, whenever a game on the feed started or finished.

Tapping a score notification launches the app with that game's id attached, and the app waits for the feed to load before it opens the game, because the game is not there to show until the feed is. It did that waiting by looking again every time the number of games on the feed changed. That number moves whenever a game starts or ends, and the feed refetches every fifteen seconds while a game is in progress. Nothing ever recorded that the notification had already been followed, so each change in the count read as a fresh tap, and a game you had closed came back on its own.

**The notification is spent the first time its game opens.** Until then the app keeps looking as the feed fills in, so a tap that arrives before the scores do still lands on the right game. After that, the id on the launch is ignored for the rest of the session, and closing the game is the end of it.

Fixes [light-reports#688] — a game opened from a notification kept reopening itself on the feed.
