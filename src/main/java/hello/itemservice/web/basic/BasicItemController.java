package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();

        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item (@PathVariable long itemId,Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }


    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,Model model){


/*
모델 어트리부트를써서 밑에꺼를 생략가능하고
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
*/

        itemRepository.save(item);

/*
내가 담은 모델에 이름과 같게 해주었기 때문에 자동으로 해주어서 이것도 생략가능함
@ModelAttribute("item")이값과 같아야한다.
        model.addAttribute("item",item);
*/
        return "basic/item";
    }



    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }


    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }


    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item saveItem = itemRepository.save(item);
//여기에 넣은 itemId이값이
        redirectAttributes.addAttribute("itemId",saveItem.getId());
        redirectAttributes.addAttribute("status",true);
//여기들어가진다{itemId}
        return "redirect:/basic/items/{itemId}" ;

    }

    @GetMapping("/{itemId}/edit")
    public String editForm( @PathVariable Long itemId,Model model){
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item",item);

        return "basic/editForm";

    }
//업데이트 기능은 모델 어트리부트에 데이터를 담아줌
    @PostMapping ("/{itemId}/edit")
    public String edit( @PathVariable Long itemId,@ModelAttribute Item item){
         itemRepository.update(itemId,item);


//post로 자꾸 보내면 상품상세로 오고 뷰 리다이렉트가 아니라 상품 상세 화면으로 리다이렉트
        return "redirect:/basic/items/" + item.getId();

    }
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}
