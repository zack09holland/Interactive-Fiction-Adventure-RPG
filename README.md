# Interactive-Fiction-Adventure-RPG
Adventure engine for a simple interactive fiction game written in Java. The game has five rooms, two objects, and allows you to move between the rooms and pick up and drop objects. 


# Instructions
1. git clone repository
2. Make sure you have java installed and set to your PATH environment variables
3. Change directory to the repository
4. Type 'java -cp src cs345.GameMain CloakHardCoded' to start the game
5. Here is a test game showcasing some of the commands you can use
```
The Cloak of Darkness (Version 0.1)

Hurrying through the rain swept November night, you're glad to see the
bright lights of the Opera House. It's surprising that there aren't more
people about; but, what do you expect in a CS assignment ...?

You are standing in a spacious hall, splendidly decorated in red and
gold, with glittering chandeliers overhead. The entrance from the street
is to the north, and there are doorways south and west.
? go s
It is dark in here!
? look
It is dark in here!
? inventory
You are carrying a velvet cloak (worn).
? drop cloak
I have a better idea: let's get out of here before you disturb anything.
? go out
You are in the hall.
? go w
The walls of this small room were clearly once lined with hooks, though
now only one remains. The exit is a door to the east. There is a brass
hook here.
? x hook
I don't understand x.
? examine hook
It's just a small brass hook, screwed into the wall.
? examine cloak
It is a handsome cloak, of velvet trimmed with satin, and slightly
spattered with raindrops. Its blackness is so deep that it almost seems
to suck light from the room.
? hang cloak
You hang the cloak on the hook.
? examine hook
It's just a small brass hook, screwed into the wall.
? go out
You are in the hall.
? go s
The bar, much rougher than you'd have guessed after the opulence of the
foyer to the north, is completely empty. There seems to be some sort of
message scrawled in the sawdust on the floor.
? look
The bar, much rougher than you'd have guessed after the opulence of the
foyer to the north, is completely empty. There seems to be some sort of
message scrawled in the sawdust on the floor.
? examine message
The message has been carelessly trampled, making it difficult to read.
You can just distinguish the words...

YOU HAVE LOST !!!
```
