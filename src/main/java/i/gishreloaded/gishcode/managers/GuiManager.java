package i.gishreloaded.gishcode.managers;

import i.gishreloaded.gishcode.gui.click.ClickGui;
import i.gishreloaded.gishcode.gui.click.base.Component;
import i.gishreloaded.gishcode.gui.click.elements.CheckButton;
import i.gishreloaded.gishcode.gui.click.elements.ComboBox;
import i.gishreloaded.gishcode.gui.click.elements.Dropdown;
import i.gishreloaded.gishcode.gui.click.elements.ExpandingButton;
import i.gishreloaded.gishcode.gui.click.elements.Frame;
import i.gishreloaded.gishcode.gui.click.elements.KeybindMods;
import i.gishreloaded.gishcode.gui.click.elements.Slider;
import i.gishreloaded.gishcode.gui.click.listener.CheckButtonClickListener;
import i.gishreloaded.gishcode.gui.click.listener.ComboBoxListener;
import i.gishreloaded.gishcode.gui.click.listener.ComponentClickListener;
import i.gishreloaded.gishcode.gui.click.listener.SliderChangeListener;
import i.gishreloaded.gishcode.gui.click.theme.dark.DarkTheme;
import i.gishreloaded.gishcode.hack.Hack;
import i.gishreloaded.gishcode.hack.HackCategory;
import i.gishreloaded.gishcode.utils.GLUtils;
import i.gishreloaded.gishcode.value.BooleanValue;
import i.gishreloaded.gishcode.value.Mode;
import i.gishreloaded.gishcode.value.ModeValue;
import i.gishreloaded.gishcode.value.NumberValue;
import i.gishreloaded.gishcode.value.Value;

/**
 * Created by Hexeption on 27/02/2017.
 */
public class GuiManager extends ClickGui {

    public void Initialization() {
        addCategoryPanels();
    }

    private void addCategoryPanels() {
        int x = 20;
        int y = 20;
        int right = GLUtils.getScreenWidth();

        for (HackCategory category : HackCategory.values()) {
                String name = Character.toString(category.toString().toLowerCase().charAt(0)).toUpperCase() + category.toString().toLowerCase().substring(1);
                Frame frame = new Frame(x, y, 95, 150, name);

                for (final Hack mod : HackManager.getHacks()) {
                    if (mod.getCategory() == category) {
                        final ExpandingButton expandingButton = new ExpandingButton(0, 0, 95, 14, frame, mod.getName(), mod) {

                            @Override
                            public void onUpdate() {
                                setEnabled(mod.isToggled());
                            }
                        };
                        expandingButton.addListner(new ComponentClickListener() {

							@Override
							public void onComponenetClick(Component component, int button) {
								mod.toggle();	
							}
                        });
                        expandingButton.setEnabled(mod.isToggled());
                        
                        if (!mod.getValues().isEmpty()) {
                            for (Value value : mod.getValues()) {
                                if (value instanceof BooleanValue) {
                                    final BooleanValue booleanValue = (BooleanValue) value;
                                    CheckButton button = new CheckButton(0, 0, expandingButton.getDimension().width, 14, expandingButton, booleanValue.getName(), booleanValue.getValue(), null);
                                    button.addListeners(new CheckButtonClickListener() {

										@Override
										public void onCheckButtonClick(CheckButton checkButton) {
											for (Value value1 : mod.getValues()) {
	                                            if (value1.getName().equals(booleanValue.getName())) {
	                                                value1.setValue(checkButton.isEnabled());
	                                            }
	                                        }
										}
                                    	
                                    });
                                    expandingButton.addComponent(button);
                                
                                } else if (value instanceof NumberValue) {
                                    final NumberValue doubleValue = (NumberValue) value;
                                    Slider slider = new Slider(doubleValue.getMin(), doubleValue.getMax(), doubleValue.getValue(), expandingButton, doubleValue.getName());
                                    slider.addListener(new SliderChangeListener() {
										@Override
										public void onSliderChange(Slider slider) {
											for (Value value1 : mod.getValues()) {
	                                            if (value1.getName().equals(value.getName())) {
	                                                value1.setValue(slider.getValue());
	                                            }
	                                        }
										}
                                    	
                                    });

                                    expandingButton.addComponent(slider);
                                
                                
                            } else if (value instanceof ModeValue) {
                            	Dropdown dropdown = new Dropdown(0, 0, 90, 14, frame, value.getName());
                            	
                            	final ModeValue modeValue = (ModeValue) value;
                            	
                            	for(Mode mode : modeValue.getModes()) {
                            		CheckButton button = new CheckButton(0, 0, 
                            				expandingButton.getDimension().width, 14, expandingButton, 
                            				mode.getName(), mode.isToggled(), modeValue);
                            		
                            			button.addListeners(checkButton -> {
                            				for(Mode mode1 : modeValue.getModes()) {
                            					if (mode1.getName().equals(mode.getName())) {
                            						mode1.setToggled(checkButton.isEnabled());
                            					}
                            				}
                 
                                    	});
                            			dropdown.addComponent(button);
                            		}
                            		expandingButton.addComponent(dropdown);
                            	}
                            }
                        }
                        KeybindMods keybind = new KeybindMods(0, 0, 8, 14, expandingButton, mod);
                        expandingButton.addComponent(keybind);
                        frame.addComponent(expandingButton);
                    }
                }

                if (x + 100 < right) {
                    x += 100;
                } else {
                    x = 20;
                    y += 60;
                }

                frame.setMaximizible(true);
                frame.setPinnable(true);
                this.addFrame(frame);
        }
    }
}
