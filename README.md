# Description
PopperFramework is a java framework allowing you to create PageObjects for your surface tests in a declarative way. This means you just have to define an interface defining the structure of your website and add some annotation with the basic information like locators. PopperFramework will generate the implementation of that PageObject for you.

PopperFramework supports Page Objects for Web via WebDriver-API and Swing via Jemmy.

## Features

Features

- Full declarative description of PageObjects by only using interfaces
- Full 
- Support for Webpages (via Webdriver) and Swing Applications (via Jemmy)
- Comprehensive errormessages when elements can't be found on page
- Support for working with tables
- Easy as can be integration into existing projects
- Unittested migration pathes from legacy PageObjects to declarative PageObjects

# Examples

## Web

    @Page
    public interface LoginSite {
      @PageAccesor(uri = "login.html")
      void open();

      @Locator(xpath = "//input[@name='username']")
      ITextBox usernameTextbox();
    
      @Locator(xpath = "//input[@name='password']")
      ITextBox passwordTextbox();
    
      @Locator(cssSelector = ".submitLoginButton")
      IButton submitLoginButton();
    }

    public class WebLoginTest {
      protected IPoFactory factory;

      @Before
      public void setup() {
        WebdriverContext context = new WebdriverContext();
        context.getDefaultConfig().setBrowser(Browser.FIREFOX);
        context.getDefaultConfig().setBaseUrl("http://some.location.com/");
        factory = context.getFactory();
      }

      @Test
      public void testLogin() {
        LoginSite login = factory.createPage(LoginSite.class);
        login.open();
        login.usernameTextbox().type("Michael");
        login.passwordTextbox().type("secret");
        login.submitLoginButton().click();
      }
    }

## Swing

    @Frame
    @Locator(title = "Example App")
    public interface LoginAppPo {
      @Locator(xpath = "//*[@name='username']")
      ITextBox usernameTextbox();
    
      @Locator(name = "password")
      ITextBox passwordTextbox();
    
      @Locator(name = "submitLogin")
      IButton submitLoginButton();
    }

    public class SwingLoginTest { 
      protected IPoFactory factory;

      @Before
      public void setup() {
        JemmyContext context = new JemmyContext(LoginApp.class);
        context.setRelevantTimeouts(500);
        factory = context.getFactory();
        context.start();
      }

      @Test
      public void testLogin() {
        LoginAppPo login = factory.createPage(LoginAppPo.class);
        login.usernameTextbox().type("Michael");
        login.passwordTextbox().type("secret");
        login.submitLoginButton().click();
      }
    }

# Getting started

When you want to use popperfw for webpages use the following Maven dependency

    <dependency>
      <groupId>org.popperfw</groupId>
      <artifactId>webdriver</artifactId>
      <version>0.8.7</version>
    </dependency>

When you want to use popperfw for swing applications use the following Maven dependency

    <dependency>
      <groupId>org.popperfw</groupId>
      <artifactId>jemmy</artifactId>
      <version>0.8.7</version>
    </dependency>

