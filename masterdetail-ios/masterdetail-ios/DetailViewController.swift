//
// Copyright 2014-2015 Kirk C. Vogen
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
//

import UIKit

class DetailViewController: UIViewController, JavaBeansPropertyChangeListener {
    
    @IBOutlet weak var listTitle: UITextField!
    
    @IBOutlet weak var words: UITextView!
    
    let detailService: DetailService
    let viewModel: DetailViewModel
    
    required init(coder decoder: NSCoder) {
        detailService = FlatFileDetailService(storageService: LocalStorageService())
        viewModel = DetailViewModel(detailService: detailService)
        super.init(coder: decoder)
    }
    
    var listId: CInt?
    
    func configureView() {
        if let id = self.listId {
            viewModel.init__WithInt(id)
            
            // No need to set field values as the PropertyChangeListener given to the view model will set the fields
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        viewModel.addPropertyChangeListenerWithJavaBeansPropertyChangeListener(self)
        // Do any additional setup after loading the view, typically from a nib.
        self.configureView()
    }

    
    override func viewWillDisappear(animated:Bool) {
        // This method will need to change if there is another view to forward to in addition to the
        // view to go back to.
        viewModel.saveWithNSString(self.listTitle.text, withNSString: self.words.text)
        super.viewWillDisappear(animated)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func propertyChangeWithJavaBeansPropertyChangeEvent(event: JavaBeansPropertyChangeEvent!) {
        
        switch event.getPropertyName() {
            
        case DetailViewModel_TITLE_:
            if let textfield = listTitle {
                if let newValue: AnyObject = event.getNewValue() {
                    textfield.text = newValue as NSString
                }
            }
        case DetailViewModel_WORDS_:
            if let textfield = words {
                if let newValue: AnyObject = event.getNewValue() {
                    textfield.text = newValue as NSString
                }
            }
        default:
            break
        }
    }
}
