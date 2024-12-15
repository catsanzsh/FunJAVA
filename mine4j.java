package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 * JMonkey-based Minecraft-like Voxel Game (mine4k).
 * Extensible to handle a complex voxel world.
 */
public class mine4k extends SimpleApplication {

    // World settings
    private static final int WORLD_SIZE = 16; // 16x16x16 grid of blocks
    private static final float BLOCK_SIZE = 1.0f;

    // Movement flags
    private boolean isForward = false;
    private boolean isBackward = false;
    private boolean isLeft = false;
    private boolean isRight = false;

    public static void main(String[] args) {
        mine4k app = new mine4k();
        app.setShowSettings(false); // Disable the settings dialog
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Set background color
        viewPort.setBackgroundColor(ColorRGBA.Cyan);

        // Generate the voxel world
        generateVoxelWorld();

        // Add key controls
        initKeys();

        // Configure the camera
        flyCam.setMoveSpeed(10f); // Adjust camera speed
    }

    /**
     * Generates a 16x16x16 block world.
     */
    private void generateVoxelWorld() {
        for (int x = 0; x < WORLD_SIZE; x++) {
            for (int y = 0; y < WORLD_SIZE / 2; y++) { // Half-height for "ground"
                for (int z = 0; z < WORLD_SIZE; z++) {
                    createBlock(x, y, z);
                }
            }
        }
    }

    /**
     * Creates a single block at the given position.
     */
    private void createBlock(int x, int y, int z) {
        // Define the block geometry
        Box blockMesh = new Box(BLOCK_SIZE / 2, BLOCK_SIZE / 2, BLOCK_SIZE / 2);
        Geometry block = new Geometry("Block", blockMesh);

        // Set block material with random color
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.randomColor());
        block.setMaterial(mat);

        // Position the block
        block.setLocalTranslation(x * BLOCK_SIZE, y * BLOCK_SIZE, z * BLOCK_SIZE);

        // Attach block to the scene
        rootNode.attachChild(block);
    }

    /**
     * Initializes key mappings for player controls.
     */
    private void initKeys() {
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));

        inputManager.addListener(actionListener, "Forward", "Backward", "Left", "Right");
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            // Set movement flags based on input
            switch (name) {
                case "Forward":
                    isForward = isPressed;
                    break;
                case "Backward":
                    isBackward = isPressed;
                    break;
                case "Left":
                    isLeft = isPressed;
                    break;
                case "Right":
                    isRight = isPressed;
                    break;
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        // Adjust movement speed
        float moveSpeed = tpf * 10;

        // Get movement directions
        Vector3f camDir = cam.getDirection().clone().multLocal(moveSpeed);
        Vector3f camLeft = cam.getLeft().clone().multLocal(moveSpeed);

        // Move camera based on input
        if (isForward) cam.setLocation(cam.getLocation().add(camDir));
        if (isBackward) cam.setLocation(cam.getLocation().subtract(camDir));
        if (isLeft) cam.setLocation(cam.getLocation().add(camLeft));
        if (isRight) cam.setLocation(cam.getLocation().subtract(camLeft));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        // Additional rendering logic can go here (currently unused)
    }
}
