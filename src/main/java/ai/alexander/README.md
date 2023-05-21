# Description of AI

## Choose the last card that determines what the next player has to react to.
- choose the card with most colours to increase the possibility that the next player has to loose cards.
- The last card's value shall never be higher than the amout of cards of the next Player.
- If possible, the AI shall not choose a card as last card, if the AI itself can fulfil the card's demand.

## Choose the cards the ai wants to give or to keep
- It might be advantegous to keep cards with only one colour. By that, the AI reduces the chance to loose card later.
- The fewer different card's the ai has, the better the change to get new cards. 


## Special Cards
### New Game Card 
- only in case of the AI would otherwise loose the last cards 
- the opponent has only one card
- the ai would otherwise loose 3 cards

### Transparency Card
- only in case the AI would otherwise loose 1 Cards (2 or 3 would be too dangerous)

### Choosing Card
- Play only in case the ai would otherwise loose
- Choose the opponent with most cards and divide the amound of cards by 4 as demand for card loss

## Furher rules for AI
- never play an action card in a set

## Contact
If you have any questions, ideas, problems, hints or comments, do not hesitate to contact the programmers using the mail adress:
alexander.lauruhn@fh-bielefeld.de

## Git
The whole program is saved in a git repository here:
https://github.com/Nope-Cardgame/KIJava

## Outlook:
Further plans are to play the game as AI and as human to improve the understanding of strategies for this game. This way,
the AI's strategy can get improved.

## Copyright
The whole code as well as the code documentation (comments, java-doc & README.md) are written by the authors.
Already existing classes like ArrayList, etc. were imported as java libraries.
