package com.example.mobilele.web;

import com.example.mobilele.model.dto.binding.offer.OfferAddBindingModel;
import com.example.mobilele.model.dto.service.offer.OfferAddServiceModel;
import com.example.mobilele.model.dto.service.offer.OfferUpdateServiceModel;
import com.example.mobilele.model.dto.view.offer.OfferViewModel;
import com.example.mobilele.model.entity.enums.EngineEnum;
import com.example.mobilele.model.entity.enums.TransmissionType;
import com.example.mobilele.service.BrandService;
import com.example.mobilele.service.ModelService;
import com.example.mobilele.service.OfferService;
import com.example.mobilele.user.CurrentUser;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OffersController {
  private final OfferService offerService;
  private final BrandService brandService;
  private final ModelMapper modelMapper;
  private final CurrentUser currentUser;
  private final ModelService modelService;

  public OffersController(OfferService offerService, BrandService brandService, ModelMapper modelMapper, CurrentUser currentUser, ModelService modelService) {
    this.offerService = offerService;
    this.brandService = brandService;
    this.modelMapper = modelMapper;
    this.currentUser = currentUser;
    this.modelService = modelService;
  }

  @ModelAttribute("offerBindingModel")
  public OfferAddBindingModel offerBindingModel() {
    return new OfferAddBindingModel();
  }

  @ModelAttribute("engines")
  public EngineEnum[] getEngines() {
    return EngineEnum.values();
  }

  @ModelAttribute("transmissions")
  public TransmissionType[] getTransmissions() {
    return TransmissionType.values();
  }

  // GET
  @GetMapping("/offers/all")
  public String getAllOffersPage(Model model) {
    List<OfferViewModel> offers = this.offerService.findAllOffers();
    model.addAttribute("offers", offers);

    return "offers";
  }

  @GetMapping("/offers")
  public String getModelsByBrandName(@RequestParam String brand, Model model) {
    List<OfferViewModel> offersByBrand = offerService
            .findOffersByBrand(brand.toLowerCase())
            .stream()
            .map(offerServiceModel -> modelMapper.map(offerServiceModel, OfferViewModel.class))
            .collect(Collectors.toList());

    model.addAttribute("offers", offersByBrand);

    return "offers";
  }

  @GetMapping("/offers/details/{id}")
  public String getOffersDetailsPage(@PathVariable Long id, Model model) {
    OfferViewModel viewModel = this.modelMapper.map(this.offerService.findOfferById(id), OfferViewModel.class);

    model.addAttribute("offer", viewModel);

    return "details";
  }

  // * UPDATE OFFER
  @GetMapping("/offers/update/{id}")
  public String getOfferUpdatePage(@PathVariable Long id, Model model) {
    OfferAddBindingModel offerBindingModel =
            this.modelMapper.map(this.offerService.findOfferById(id), OfferAddBindingModel.class);

    model.addAttribute("models", this.modelService.findModelsPerBrand(offerBindingModel.getBrand()));
    model.addAttribute("offerBindingModel", offerBindingModel);

    return "update";
  }

  @PatchMapping("/offers/update/{id}")
  public String updateOffer(@PathVariable Long id,
                            @Valid OfferAddBindingModel offerAddBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      redirectAttributes
              .addFlashAttribute("offerBindingModel", offerAddBindingModel)
              .addFlashAttribute("org.springframework.validation.BindingResult.offerBindingModel", bindingResult);
      return "redirect:/offers/update/" + id;
    }

    this.offerService.updateOffer(this.modelMapper.map(offerAddBindingModel, OfferUpdateServiceModel.class), currentUser.getId());

    return "redirect:/offers/details/" + id;
  }

  // ADD OFFER
  @GetMapping("/offers/add")
  public String getAddOffersPage(Model model) {
    model.addAttribute("brands", this.brandService.findAllBrands());
//    model.addAttribute("engines", EngineEnum.values());
//    model.addAttribute("transmissions", TransmissionType.values());

    return "offer-add";
  }

  @PostMapping("/offers/add")
  public String addOffer(@Valid OfferAddBindingModel offerAddBindingModel,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      redirectAttributes
              .addFlashAttribute("offerBindingModel", offerAddBindingModel)
              .addFlashAttribute("org.springframework.validation.BindingResult.offerBindingModel", bindingResult);
//              .addFlashAttribute("brands", this.brandService.findAllBrands());
      return "redirect:/offers/add";
    }

    OfferAddServiceModel serviceModel =
            this.offerService.addOffer(
                    this.modelMapper.map(offerAddBindingModel, OfferAddServiceModel.class),
                    currentUser.getId());

    return "redirect:/offers/details/" + serviceModel.getId();
  }

  // DELETE OFFER
  @DeleteMapping("/offers/{id}")
  public String deleteOfferById(@PathVariable Long id) {
    this.offerService.deleteById(id);

    return "redirect:/offers/all";
  }
}
