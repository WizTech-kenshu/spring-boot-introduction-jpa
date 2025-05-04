package com.example.todo.controller.category;

import com.example.todo.controller.task.TaskDTO;
import com.example.todo.controller.task.TaskForm;
import com.example.todo.controller.task.TaskNotFoundException;
import com.example.todo.entity.Category;
import com.example.todo.entity.Task;
import com.example.todo.service.category.CategoryService;
import com.example.todo.service.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(CategoryForm form, Model model) {
        System.out.println("Categories!");
        /*var categoryList = categoryService.find()
                .stream()
                .map(CategoryDTO::toDTO)
                .toList();*/
        var categoryList = categoryService.findAll();
        System.out.println(categoryList);
        model.addAttribute("categoryList", categoryList);


        return "categories/list";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") long searchId, Model model) {
        System.out.println(searchId);

        var categoryEntity =  categoryService.findById(searchId).orElse(null);

        System.out.println(categoryEntity);

        if(categoryEntity == null){
            return "redirect:/";
        }
        model.addAttribute("id", searchId);
        model.addAttribute("categoryEntity", categoryEntity);


        return "categories/detail";
    }

    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute CategoryForm form, Model model) {
        model.addAttribute("mode", "CREATE");


        return "categories/form";
    }

    @PostMapping
    public String create(@Validated CategoryForm form,BindingResult bindingResult, Model model) {
        System.out.println(form);

        if (bindingResult.hasErrors()) {
            return showCreationForm(form, model);
        }
        categoryService.create(form.toEntity());

        model.addAttribute("number", 100);


        return "redirect:/categories";
    }

    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        Category categoryEntity =  categoryService.findById(id).orElse(null);

        CategoryForm categoryForm = CategoryForm.fromEntity(categoryEntity);



        model.addAttribute("mode", "EDIT");
        model.addAttribute("categoryForm", categoryForm);
        return "categories/form";
    }

    @PutMapping("{id}") // PUT /caterories/{id}
    public String update(
            @PathVariable("id") long id,
            @Validated @ModelAttribute CategoryForm form,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "EDIT");
            return "categories/form";
        }

        Category entity  = CategoryService.findById(id).get();
        //var entity = form.toEntity(id);
        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        //entity.setCategory_id(form.category_id());

        CategoryService.update(entity);
        return "redirect:/tasks/{id}";


        return "redirect:/categories/{id}";
    }

    // POST /tasks/1 (hidden: _method: delete)
    // -> DELETE /tasks/1
    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id) {

        return "redirect:/categories";
    }
}