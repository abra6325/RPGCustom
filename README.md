# Relizc Network Dungeon MMORPG Game.
THIS IS A ThING U CAN USE TO MAKE A PLUGIN FOR RPF

## Server RAM Categorization
`T` **Tiny Server**: Server RAM < 256 MB (262,144 Bytes)<br>
`S` **Small Server**: Server RAM >= 256 MB (262,144 Bytes) < 512 MB (524,288 Bytes)<br>
`M` **Medium Server**: Server RAM >= 512 MB (524,288 Bytes) < 1 GB (1,048,756 Bytes)<br>
`B` **Big Server**: Server RAM >= 1GB (1,048,756) < 8 GB (8,388,608 Bytes)<br>
`G` **Gigantic Server**: Server RAM >= 8GB (8,388,608 Bytes)

## Story Creating
Start by binding a speaker to a dialogue variable by using `SPEAKER sk0`
This initlizes the speaker called sk0. It could be binded later in the scene class by using `net.itsrelizc.scene.SceneLoader.attachDialogue(xyz.story)`

## Animation Creating
Please note that **DO NOT HAND WRITE ANY ANIMATION FILE LIKE THIS**
In the future, Relizc Network will release a specific animation recorder that automatically converts player's actions into an animation file.
Attach or play an animation in a scene by using `net.itsrelizc.scene.Scene#.attachAnimation(AnimationFile file)`. This will return a pending animation class which programmers could adjust the animation location.

## Scene Creating
This is the most complicated part in the creation process. The scene is no longer a file with a bunch of commands anymore. Instead, it is intended to implement the `net.itsrelizc.scene.Scene` class. Users could `this.attachAnimation(AnimationFile file)` to attach an animation and use the above story creating method to attach dialogues. Therefore, the scene files are hard-coded into the package `net.itsrelizc.scenes`.
