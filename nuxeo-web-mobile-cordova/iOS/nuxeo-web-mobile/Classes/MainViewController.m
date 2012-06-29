/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

//
//  MainViewController.h
//  nuxeo-web-mobile
//
//  Created by Arnaud Kervern on 6/5/12.
//  Copyright __MyCompanyName__ 2012. All rights reserved.
//

#import "MainViewController.h"

@interface MainViewController ()
-(NSString *)fileToBeExecutedWith:(NSString *)name inWebView:(UIWebView*)aWebView;
@end

@implementation MainViewController

- (id) initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void) didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void) viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void) viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL) shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return [super shouldAutorotateToInterfaceOrientation:interfaceOrientation];
}

/* Comment out the block below to over-ride */
/*
- (CDVCordovaView*) newCordovaViewWithFrame:(CGRect)bounds
{
    return[super newCordovaViewWithFrame:bounds];
}
*/

/* Comment out the block below to over-ride */
/*
#pragma CDVCommandDelegate implementation

- (id) getCommandInstance:(NSString*)className
{
	return [super getCommandInstance:className];
}

- (BOOL) execute:(CDVInvokedUrlCommand*)command
{
	return [super execute:command];
}

- (NSString*) pathForResource:(NSString*)resourcepath;
{
	return [super pathForResource:resourcepath];
}
 
- (void) registerPlugin:(CDVPlugin*)plugin withClassName:(NSString*)className
{
    return [super registerPlugin:plugin withClassName:className];
}
*/

#pragma UIWebDelegate implementation

- (void) webViewDidFinishLoad:(UIWebView*) theWebView 
{
     // only valid if ___PROJECTNAME__-Info.plist specifies a protocol to handle
     if (self.invokeString)
     {
        // this is passed before the deviceready event is fired, so you can access it in js when you receive deviceready
        NSString* jsString = [NSString stringWithFormat:@"NXCordova.handleOpenURL('%@');", self.invokeString];
        [theWebView stringByEvaluatingJavaScriptFromString:jsString];
     }
     
     // Black base color for background matches the native apps
     theWebView.backgroundColor = [UIColor blackColor];
    
    // Add custom JS
    NSURL *baseDirURL = [[NSBundle mainBundle] URLForResource:self.wwwFolderName withExtension:nil];
    
    [theWebView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"var cordovaBase = '%@'", baseDirURL.absoluteURL]];
    
    [self fileToBeExecutedWith:@"scripts/ChildBrowser.js" inWebView:theWebView];
    [self fileToBeExecutedWith:@"scripts/NxFileBrowser.js" inWebView:theWebView];
    [self fileToBeExecutedWith:@"scripts/nuxeo-cordova-wrapper.js" inWebView:theWebView];
    //NSLog(@"URL: %@", [theWebView.request.URL absoluteString]);
	return [super webViewDidFinishLoad:theWebView];
}

-(NSString *)fileToBeExecutedWith:(NSString *)name inWebView:(UIWebView*)aWebView {
    NSURL *url = [NSURL fileURLWithPath:[self pathForResource:name]];
    //NSLog(@"file: %@", url.absoluteURL);
    
    NSError *error;
    
    return [aWebView stringByEvaluatingJavaScriptFromString:[NSString stringWithContentsOfURL:url encoding:NSUTF8StringEncoding error:&error]];
}

- (void) webViewDidStartLoad:(UIWebView*)theWebView 
{
    //NSLog(@"%@", NSStringFromSelector(@selector(webViewDidFinishLoad:)));
	return [super webViewDidStartLoad:theWebView];
}

- (void) webView:(UIWebView*)theWebView didFailLoadWithError:(NSError*)error 
{
    //NSLog(@"%@", NSStringFromSelector(@selector(webView:didFailLoadWithError:)));
	return [super webView:theWebView didFailLoadWithError:error];
}

- (BOOL) webView:(UIWebView*)theWebView shouldStartLoadWithRequest:(NSURLRequest*)request navigationType:(UIWebViewNavigationType)navigationType
{
    // Do not handle nxbigfile in Cordova
    NSURL *url = request.URL;
    if ([url.absoluteString rangeOfString:@"nxbigfile"].location != NSNotFound) {
        [[UIApplication sharedApplication] openURL:url];
        return NO;
    }
    
    //NSLog(@"%@", NSStringFromSelector(@selector(webView:shouldStartLoadWithRequest:navigationType:)));
    //NSLog(@"Try to open: %@", request.URL.absoluteURL);
	return [super webView:theWebView shouldStartLoadWithRequest:request navigationType:navigationType];
}

@end
