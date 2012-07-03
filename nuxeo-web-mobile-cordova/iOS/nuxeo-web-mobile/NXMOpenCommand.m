//
//  NXMOpenCommand.m
//  nuxeo-web-mobile
//
//  Created by Arnaud Kervern on 6/27/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "NXMOpenCommand.h"
#import "NSData+Additions.h"

@implementation NXMOpenCommand
-(void)openURL:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options; {
    NSString* url = [arguments objectAtIndex:1 withDefault:@"about:blank"];
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
    [self.webView loadRequest:request];
    [request release];
}

-(void)openServer:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options {
    NSString* url = [arguments objectAtIndex:1 withDefault:@"about:blank"];
    NSString* login = [arguments objectAtIndex:2 withDefault:nil];
    NSString* password = [arguments objectAtIndex:3 withDefault:nil];
    
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:url]];
    if (login != nil && password != nil) {
        NSString *authStr = [NSString stringWithFormat:@"%@:%@", login, password];
        NSData *authData = [authStr dataUsingEncoding:NSUTF8StringEncoding];
        NSString *authValue = [NSString stringWithFormat:@"basic %@", [authData base64Encoding]];
        [request setValue:authValue forHTTPHeaderField:@"authorization"];
        
        //NSString *loginUrl = [NSString stringWithFormat:@"%@%@", request.URL.absoluteString, @"/site/mobile/auth/login"];
        NSString *loginUrl = [NSString stringWithFormat:@"%@%@", request.URL.absoluteString, @"/site/mobile"];
        [request setURL:[NSURL URLWithString:loginUrl]];
    }
    [self.webView loadRequest:request];
    [request release];    
}
@end
