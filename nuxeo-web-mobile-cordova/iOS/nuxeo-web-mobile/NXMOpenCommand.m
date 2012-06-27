//
//  NXMOpenCommand.m
//  nuxeo-web-mobile
//
//  Created by Arnaud Kervern on 6/27/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "NXMOpenCommand.h"

@implementation NXMOpenCommand
-(void)openURL:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options; {
    NSString* url = [arguments objectAtIndex:0 withDefault:@"about:blank"];
    
    NSURLRequest* request = [[NSURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
    [self.webView loadRequest:request];
    [request release];
}
@end
