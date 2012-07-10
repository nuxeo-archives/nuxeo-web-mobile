//
//  NXMOpenCommand.h
//  nuxeo-web-mobile
//
//  Created by Arnaud Kervern on 6/27/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifdef CORDOVA_FRAMEWORK
#import <Cordova/CDVPlugin.h>
#else
#import "Cordova/CDVPlugin.h"
#endif

@interface NXMOpenCommand : CDVPlugin<UIActionSheetDelegate, UIDocumentInteractionControllerDelegate>
-(void)openURL:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;

-(void)openServer:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
@end
